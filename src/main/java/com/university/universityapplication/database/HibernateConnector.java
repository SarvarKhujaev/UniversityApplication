package com.university.universityapplication.database;

import com.university.universityapplication.entities.query_result_mapper_entities.TeacherAverageMark;
import com.university.universityapplication.constans.postgres_constants.PostgresBufferMethods;
import com.university.universityapplication.constans.postgres_constants.PostgresVacuumMethods;
import com.university.universityapplication.constans.hibernate.HibernateNativeNamedQueries;
import com.university.universityapplication.constans.postgres_constants.PostgreSqlTables;
import com.university.universityapplication.constans.postgres_constants.PostgreSqlSchema;
import com.university.universityapplication.interfaces.ServiceCommonMethods;
import com.university.universityapplication.inspectors.Archive;
import com.university.universityapplication.entities.*;

import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.stat.CacheRegionStatistics;
import org.hibernate.boot.MetadataSources;
import org.hibernate.*;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.Validation;

import java.text.MessageFormat;
import java.util.List;
import java.util.Set;

public final class HibernateConnector extends Archive implements ServiceCommonMethods {
    public Session getSession() {
        return this.session;
    }

    public SessionFactory getSessionFactory() {
        return this.sessionFactory;
    }

    public StandardServiceRegistry getRegistry() {
        return this.registry;
    }

    public ValidatorFactory getValidatorFactory() {
        return this.validatorFactory;
    }

    private final Session session;
    private final SessionFactory sessionFactory;
    private final StandardServiceRegistry registry;
    private final ValidatorFactory validatorFactory;

    private static HibernateConnector CONNECTOR = new HibernateConnector();

    public static HibernateConnector getInstance() {
        return CONNECTOR != null ? CONNECTOR : ( CONNECTOR = new HibernateConnector() );
    }

    private Transaction newTransaction () {
        return this.getSession().beginTransaction();
    }

    private HibernateConnector() {
        /*
        оегистрируем все настройки
        */
        this.registry = new StandardServiceRegistryBuilder()
                .applySettings( super.dbSettings )
                .build();

        /*
        подключаемся к самой БД
        */
        this.sessionFactory = new MetadataSources( this.getRegistry() )
                .addAnnotatedClass( Group.class )
                .addAnnotatedClass( Lesson.class )
                .addAnnotatedClass( Teacher.class )
                .addAnnotatedClass( Student.class )
                .addAnnotatedClass( Comment.class )
                .addAnnotatedClass( EducationDirection.class )
                .addAnnotatedClass( StudentAppearanceInLessons.class )
                .getMetadataBuilder()
                .build()
                .getSessionFactoryBuilder()
                .build();

        /*
        открываем сессию
        */
        this.session = this.getSessionFactory().openSession();

        /*
        создаем instance класса Validation для валидации объектов
         */
        this.validatorFactory = Validation.buildDefaultValidatorFactory();

        /*
        настраиваем Second Level Cache
        */
        this.getSessionFactory().getCache().evictEntityData( Group.class );
        this.getSessionFactory().getCache().evictEntityData( Lesson.class );
        this.getSessionFactory().getCache().evictEntityData( Teacher.class );
        this.getSessionFactory().getCache().evictEntityData( Student.class );
        this.getSessionFactory().getCache().evictEntityData( Comment.class );
        this.getSessionFactory().getCache().evictEntityData( EducationDirection.class );
        this.getSessionFactory().getCache().evictEntityData( StudentAppearanceInLessons.class );

        /*
        Hibernate specific JDBC batch size configuration on a per-Session basis
         */
        this.getSession().setJdbcBatchSize( super.BATCH_SIZE );

        this.prewarmTable();

        this.insertTableContentToBuffer();

        super.logging( this.getClass() );
    }

    @Override
    public void vacuumTable () {
        super.analyze(
                super.getTablesList(),
                table -> super.logging(
                        table
                                + " was cleaned: "
                                + this.getSession().createNativeQuery(
                                        MessageFormat.format(
                                                """
                                                VACUUM( {0}, {1} ) {2}.{3}
                                                """,
                                                PostgresVacuumMethods.ANALYZE,
                                                PostgresVacuumMethods.VERBOSE,

                                                PostgreSqlSchema.UNIVERSITY,
                                                table
                                        )
                        ).getQueryString()
                )
        );
    }

    @Override
    public void prewarmTable () {
        /*
        прогреваем кэш
        */
        super.logging(
                PostgresBufferMethods.PREWARM_TABLE
                + " : "
                + this.getSession().createQuery(
                        PostgresBufferMethods.PREWARM_TABLE
                ).getSingleResult()
        );
    }

    @Override
    public void insertTableContentToBuffer () {
        final Transaction transaction = this.newTransaction();

        /*
        создаем расширение, меняем настройки pg_config и перезапускаем БД
        */
        super.logging(
                PostgresBufferMethods.CREATE_EXTENSION_FOR_BUFFER_WARMING
                        + " : "
                        + this.getSession().createQuery(
                                PostgresBufferMethods.CREATE_EXTENSION_FOR_BUFFER_WARMING
                ).getSingleResult()
        );

        /*
        загружаем список таблиц в буферы
        */
        super.analyze(
                super.getTablesList(),
                table -> super.logging(
                        table
                                + " was inserted into buffer: "
                                + this.getSession().createQuery(
                                PostgresBufferMethods.INSERT_TABLE_CONTENT_INTO_BUFFER.formatted(
                                        table
                                )
                        ).getQueryString()
                )
        );

        transaction.commit();
        super.logging( transaction );
    }

    private void checkBatchLimit () {
        if ( super.isBatchLimitNotOvercrowded() ) {
            /*
            если да, то освобождаем пространство в кеше
            на уровне first-level cache

            When you make new objects persistent, employ methods flush() and clear() to the session regularly,
            to control the size of the first-level cache.
             */
            this.getSession().flush();
            this.getSession().clear();
        }
    }

    public void getWithNativeQuery () {
        /*
        To avoid the overhead of using ResultSetMetadata, or simply to be more explicit in what is returned, one can use addScalar():

        Example;
            List<Object[]> persons = session.createNativeQuery(
                "SELECT * FROM Person", Object[].class)
            .addScalar("id", StandardBasicTypes.LONG)
            .addScalar("name", StandardBasicTypes.STRING)
            .list();
         */
        super.analyze(
                this.getSession().createNativeQuery(
                        MessageFormat.format(
                                """
                                SELECT * FROM {0}.{1}
                                WHERE id = {2};
                                """,
                                "entities",
                                "orders",
                                5
                        ),
                        Object[].class
                ).addScalar( "id", Long.class )
                        .addScalar( "total_order_sum", Long.class )
                        .getResultList(),
                objects -> super.logging( objects[0] + " : " + objects[1] )
        );
    }

    /*
    If you enable the hibernate.generate_statistics configuration property,
    Hibernate will expose a number of metrics via SessionFactory.getStatistics().
    Hibernate can even be configured to expose these statistics via JMX.

    This way, you can get access to the Statistics class which comprises all sort of second-level cache metrics.
    */
    public void readCacheStatistics () {
        final CacheRegionStatistics regionStatistics = this.getSession()
                .getSessionFactory()
                .getStatistics()
                .getDomainDataRegionStatistics(
                        super.generateCacheName(
                                PostgreSqlTables.STUDENTS
                        )
                );

        super.logging(
                regionStatistics.getRegionName()
        );

        super.logging(
                regionStatistics.getHitCount()
        );

        super.logging(
                regionStatistics.getMissCount()
        );

        super.logging(
                regionStatistics.getSizeInMemory()
        );
    }

    public void save ( final Student student ) {
        final Set< ConstraintViolation< Student > > violations = super.checkEntityValidation(
                this.getValidatorFactory().getValidator(),
                student
        );

        if ( !super.isCollectionNotEmpty( violations ) ) {
            final Transaction transaction = this.newTransaction();

            this.getSession().persist( student );

            transaction.commit();
        } else {
            super.analyze(
                    violations,
                    userConstraintViolation -> super.logging( userConstraintViolation.getMessage() )
            );
        }
    }

    public void save ( final Teacher teacher ) {
        final Set< ConstraintViolation< Teacher > > violations = super.checkEntityValidation(
                this.getValidatorFactory().getValidator(),
                teacher
        );

        if ( !super.isCollectionNotEmpty( violations ) ) {
            final Transaction transaction = this.newTransaction();

            this.getSession().save( teacher );

            transaction.commit();
        } else {
            super.analyze(
                    violations,
                    userConstraintViolation -> super.logging( userConstraintViolation.getMessage() )
            );
        }
    }

    public void save ( final Group group ) {
        final Set< ConstraintViolation< Group > > violations = super.checkEntityValidation(
                this.getValidatorFactory().getValidator(),
                group
        );

        if ( !super.isCollectionNotEmpty( violations ) ) {
            final Transaction transaction = this.newTransaction();

            this.getSession().persist( group );

            transaction.commit();
        } else {
            super.analyze(
                    violations,
                    userConstraintViolation -> super.logging( userConstraintViolation.getMessage() )
            );
        }
    }

    public void insertStudents () {
        final List< EducationDirection > educationDirectionList = this.getSession().createQuery(
                "FROM EDUCATION_DIRECTIONS"
        ).getResultList();

        for ( int i = 0; i < 5; i++ ) {
            this.checkBatchLimit();
            final Student student = new Student();

            student.setAge( (byte) 25 );
            student.setName( "test" + i );
            student.setEmail( i + "test@gmail.com" );
            student.setSurname( "test" + i );
            student.setBirthDate( "test" + i );
            student.setFatherName( "test" + i );
            student.setStudentShortDescription( "test" + i );

            student.setEducationDirectionList( educationDirectionList );

            this.save( student );
        }
    }

    public void insertTeachers () {
        for ( int i = 0; i < 5; i++ ) {
            this.checkBatchLimit();

            final Teacher teacher = new Teacher();

            teacher.setAge( (byte) 25 );
            teacher.setName( "test" + i );
            teacher.setEmail( i + "test@gmail.com" );
            teacher.setSurname( "test" + i );
            teacher.setBirthDate( "test" + i );
            teacher.setFatherName( "test" + i );
            teacher.setTeacherShortDescription( "test" + i );

            this.save( teacher );
        }
    }

    public void insertGroups () {
        final Transaction transaction = this.newTransaction();

        final List< Student > students = this.getSession().createQuery(
                "FROM STUDENTS"
        ).getResultList();

        final List< Teacher > teachers = this.getSession().createQuery(
                "FROM TEACHERS"
        ).getResultList();

        final List< EducationDirection > educationDirectionList = this.getSession().createQuery(
                "FROM EDUCATION_DIRECTIONS"
        ).getResultList();

        for ( int i = 0; i < 5; i++ ) {
            this.checkBatchLimit();

            final Group group = new Group();

            group.setTeacher( teachers.get( i ) );
            group.setGroupName( "testGroup: " + i );
            group.setMaxStudentsNumber( (byte) 5 );
            group.setEducationDirection( educationDirectionList.get( i ) );

            this.getSession().persist( teachers.get( i ) );

            students.get( i ).addNewGroup( group );

            this.getSession().persist( students.get( i ) );

            this.getSession().persist( group );
        }

        transaction.commit();
    }

    public void save () {
        final Transaction transaction = this.newTransaction();

        final Group group = this.getSession().get( Group.class, 9L );

        for ( int i = 0; i < 5; i++ ) {
            this.checkBatchLimit();

            final Lesson lesson = new Lesson();

            lesson.setLessonName( "test" + i );

            group.getLessonList().add( lesson );

            this.getSession().persist( lesson );
        }

        this.getSession().update( group );

        transaction.commit();

        super.logging( transaction );
    }

    public void saveComments () {
        final Transaction transaction = this.newTransaction();

        final Lesson lesson = this.getSession().get(
                Lesson.class,
                11L
        );

        final List< Student > students = this.getSession().createQuery(
                "FROM STUDENTS",
                Student.class
        ).getResultList();

        for ( int i = 0; i < students.size(); i++ ) {
            this.checkBatchLimit();

            final Comment comment = new Comment();

            comment.setStudent( students.get( i ) );
            comment.setComment( "test" + i );
            comment.setLesson( lesson );

            lesson.getCommentList().add( comment );

            this.getSession().persist( comment );
        }

        this.getSession().update( lesson );

        transaction.commit();

        super.logging( transaction );
    }

    public void test () {
        final Transaction transaction = this.newTransaction();

        final List< Student > students = this.getSession().createQuery(
                "FROM STUDENTS WHERE id IN ( 7, 8, 9, 10 )"
        ).getResultList();

        final Group group = this.getSession().get( Group.class, 9L );

        super.analyze(
                students,
                student -> student.addNewGroup( group )
        );

        super.analyze(
                students,
                this.getSession()::persist
        );

        this.getSession().update( group );

        transaction.commit();
    }

    public void getGroup () {
        final TeacherAverageMark teacherAverageMark = this.getSession().createNamedQuery(
                HibernateNativeNamedQueries.GET_TEACHER_AVERAGE_MARKS,
                TeacherAverageMark.class
        ).setParameter( "teacher_id", 1 )
                .setCacheable( true )
                .setCacheMode( CacheMode.GET )
                .setCacheRegion(
                        super.generateCacheName( PostgreSqlSchema.UNIVERSITY )
                ).getSingleResult();

        System.out.println(
                teacherAverageMark.getAverageMarkNaming()
                + " : "
                + teacherAverageMark.getAverageMark()
                + " : "
                + teacherAverageMark.getLessonCount()
        );
    }

    /*
    закрывам все соединения и instance
    */
    @Override
    public synchronized void close () {
        this.getSession().clear();
        this.getSession().close();
        this.getSessionFactory().close();
        this.getValidatorFactory().close();
        StandardServiceRegistryBuilder.destroy( this.getRegistry() );

        CONNECTOR = null;

        super.logging( this );
    }
}
