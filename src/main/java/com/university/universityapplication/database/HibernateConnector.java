package com.university.universityapplication.database;

import com.university.universityapplication.constans.postgres_constants.postgres_prepared_constants.PostgresPreparedQueryParams;
import com.university.universityapplication.constans.postgres_constants.postgres_statistics_constants.PostgresStatisticsParams;
import com.university.universityapplication.constans.postgres_constants.postgres_prepared_constants.PostgresPreparedQueryNames;
import com.university.universityapplication.entities.query_result_mapper_entities.TeacherAverageMark;
import com.university.universityapplication.constans.postgres_constants.PostgresCommonCommands;
import com.university.universityapplication.constans.postgres_constants.PostgresBufferMethods;
import com.university.universityapplication.constans.postgres_constants.PostgresCreateValues;
import com.university.universityapplication.constans.hibernate.HibernateNativeNamedQueries;
import com.university.universityapplication.constans.postgres_constants.PostgreSqlTables;
import com.university.universityapplication.interfaces.ServiceCommonMethods;
import com.university.universityapplication.inspectors.Archive;
import com.university.universityapplication.entities.*;

import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.MetadataSources;
import org.hibernate.*;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ValidatorFactory;
import jakarta.persistence.FlushModeType;
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

    private synchronized Transaction newTransaction () {
        return this.getSession().beginTransaction();
    }

    private HibernateConnector () {
        /*
        оегистрируем все настройки
        */
        this.registry = new StandardServiceRegistryBuilder()
                .applySettings( super.dbSettings )
                .build();

        final MetadataSources metadataSources = new MetadataSources( this.getRegistry() );

        super.analyze(
                super.getClassesReferences(),
                metadataSources::addAnnotatedClass
        );

        /*
        подключаемся к самой БД
        */
        this.sessionFactory = metadataSources
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
        super.analyze(
                super.getClassesReferences(),
                clazz -> this.getSessionFactory().getCache().evictEntityData( clazz )
        );

        /*
        Hibernate specific JDBC batch size configuration on a per-Session basis
        */
        this.getSession().setJdbcBatchSize( super.BATCH_SIZE );

        this.setSessionProperties();
        this.registerAllServices();

        super.logging( this.getClass() );

        PostgresStatisticsQueryController.generate( this.getSession() ).readPgStatTuple();
        PostgresStatisticsQueryController.generate( this.getSession() ).readPgStatIndex();
    }

    private void setSessionProperties () {
        /*
        меняем настройки кластера, только в рамках сессии
        */
        this.getSession().setProperty(
                "shared_preload_libraries",
                String.join(
                        ", ",
                        PostgresBufferMethods.PG_PREWARM, // расширение для прогрева буфера
                        PostgresStatisticsParams.PG_STAT_STATEMENTS // расгирение для работы со статистикой
                )
        );

        /*
        настраиваем работу с Prepared statements
        */
        this.getSession().setProperty(
                PostgresPreparedQueryParams.PLAN_CACHE_MODE,
                "'force_custom_plan'"
        );

        /*
        сохраняем временные настройки для сбора статистики
        */
        this.getSession().setProperty( PostgresStatisticsParams.TRACK_FUNCTIONS, "all" );
        this.getSession().setProperty( PostgresStatisticsParams.TRACK_ACTIVITIES, "on" );
    }

    /*
    создаем и регистрируем все сервисы, параметры и расщирения
    */
    private void registerAllServices () {
        final Transaction transaction = this.newTransaction();

        /*
        создаем все расширения
        */
        PostgresExtensionsRegister.generate( this.getSession() );

        /*
        создаем все индексы
        */
        PostgresIndexesRegister
                .generate( this.getSession() )
                .createIndex();

        /*
        создаем таблицы для хранения статистики по всем таблицам
        */
        PostgresStatisticsTableRegister.generate( this.getSession() );

        /*
        создаем все функции в БД
        */
        PostgresFunctionsRegister.generate( this.getSession() ).createAllFunctions();

        /*
        создаем и прогреваем буферы кэша
        */
        PostgresBufferRegister.generate( this.getSession() );

        /*
        создаем все Materialized Views
        */
        PostgresMaterializedViewRegister
                .generate( this.getSession() )
                .createAllMaterializedViews();

        /*
        подгатавливаем все основные запросы для дальнейшей обработки
        */
        PostgresPrepareStatementsRegister.generate( this.getSession() );

        transaction.commit();

        super.logging( transaction );
    }

    private synchronized void checkBatchLimit () {
        if ( super.isBatchLimitNotOvercrowded() ) {
            /*
            если да, то освобождаем пространство в кеше
            на уровне first-level cache

            When you make new objects persistent, employ methods flush() and clear() to the session regularly,
            to control the size of the first-level cache.
             */
            this.getSession().flush();
        }
    }

    public synchronized void getWithNativeQuery () {
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
                        .setFlushMode( FlushModeType.COMMIT )
                        .getResultList(),
                objects -> super.logging( objects[0] + " : " + objects[1] )
        );
    }

    public synchronized void save ( final Student student ) {
        final Set< ConstraintViolation< Student > > violations = super.checkEntityValidation(
                this.getValidatorFactory().getValidator(),
                student
        );

        if ( !super.isCollectionNotEmpty( violations ) ) {
            final Transaction transaction = this.newTransaction();

            this.getSession().persist( student );

            transaction.commit();

            super.logging( transaction );
        } else {
            super.analyze(
                    violations,
                    userConstraintViolation -> super.logging( userConstraintViolation.getMessage() )
            );
        }
    }

    public synchronized void save ( final Teacher teacher ) {
        final Set< ConstraintViolation< Teacher > > violations = super.checkEntityValidation(
                this.getValidatorFactory().getValidator(),
                teacher
        );

        if ( !super.isCollectionNotEmpty( violations ) ) {
            final Transaction transaction = this.newTransaction();

            this.getSession().save( teacher );

            transaction.commit();

            super.logging( transaction );
        } else {
            super.analyze(
                    violations,
                    userConstraintViolation -> super.logging( userConstraintViolation.getMessage() )
            );
        }
    }

    public synchronized void save ( final Group group ) {
        final Set< ConstraintViolation< Group > > violations = super.checkEntityValidation(
                this.getValidatorFactory().getValidator(),
                group
        );

        if ( !super.isCollectionNotEmpty( violations ) ) {
            final Transaction transaction = this.newTransaction();

            this.getSession().persist( group );

            transaction.commit();

            super.logging( transaction );
        } else {
            super.analyze(
                    violations,
                    userConstraintViolation -> super.logging( userConstraintViolation.getMessage() )
            );
        }
    }

    public synchronized void insertStudents () {
        final Transaction transaction = this.newTransaction();

        final List< EducationDirection > educationDirectionList = this.getSession().createQuery(
                "FROM EDUCATION_DIRECTIONS"
        ).setCacheMode( CacheMode.GET )
                .setCacheable( true )
                .setHibernateFlushMode( FlushMode.AUTO )
                .getResultList();

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

        transaction.commit();

        super.logging( transaction );
    }

    public synchronized void insertTeachers () {
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

    public synchronized void insertGroups () {
        final Transaction transaction = this.newTransaction();

        final List< Student > students = this.getSession().createQuery(
                "FROM STUDENTS"
        ).setCacheable( true )
                .setCacheMode( CacheMode.GET )
                .getResultList();

        final List< Teacher > teachers = this.getSession().createQuery(
                "FROM TEACHERS"
        ).setCacheable( true )
                .setCacheMode( CacheMode.GET )
                .getResultList();

        final List< EducationDirection > educationDirectionList = this.getSession().createQuery(
                "FROM EDUCATION_DIRECTIONS"
        ).setCacheable( true )
                .setCacheMode( CacheMode.GET )
                .getResultList();

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
        super.logging( transaction );
    }

    public synchronized void save () {
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

    public synchronized void saveComments () {
        final Transaction transaction = this.newTransaction();

        final Lesson lesson = this.getSession().get(
                Lesson.class,
                11L
        );

        final List< Student > students = this.getSession().createQuery(
                "FROM STUDENTS",
                Student.class
        ).setCacheable( true )
                .setCacheMode( CacheMode.GET )
                .setHibernateFlushMode( FlushMode.MANUAL )
                .getResultList();

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

    public synchronized void test () {
        final Transaction transaction = this.newTransaction();

        final List< Student > students = this.getSession().createQuery(
                "FROM STUDENTS WHERE id IN ( 7, 8, 9, 10 )"
        ).setCacheable( true )
                .setCacheMode( CacheMode.GET )
                .setHibernateFlushMode( FlushMode.COMMIT )
                .getResultList();

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

    public synchronized void getGroup ( final int teacherId ) {
        final TeacherAverageMark teacherAverageMark = this.getSession().createNamedQuery(
                HibernateNativeNamedQueries.GET_TEACHER_AVERAGE_MARKS,
                TeacherAverageMark.class
        ).setParameter( "teacher_id", teacherId )
                .setCacheable( true )
                .setCacheMode( CacheMode.GET )
                .getSingleResult();

        super.logging( teacherAverageMark.toString() );
    }

    public synchronized void testPreparedStatement () {
        super.analyze(
                this.getSession().createNativeQuery(
                        PostgresPreparedQueryParams.EXECUTE.formatted(
                                PostgresPreparedQueryNames.GET_TEACHERS_BY_NAME_AND_EMAIL,
                                "'test', '0test@gmail.com'"
                        ),
                        Teacher.class
                ).getResultList(),
                teacher -> super.logging( teacher.getEmail() )
        );
    }

    public synchronized void getFromView () {
        super.analyze(
                this.getSession().createNativeQuery(
                        String.join(
                                " ",
                                PostgresCommonCommands.SELECT_FROM,
                                String.join(
                                        "_",
                                        PostgreSqlTables.STUDENTS,
                                        PostgresCreateValues.MATERIALIZED_VIEW.name()
                                )
                        ),
                        Student.class
                ).getResultList(),
                student -> super.logging( student.getEmail() )
        );
    }

    /*
    закрывам все соединения и instance
    */
    @Override
    public synchronized void close () {
        PostgresVacuumImpl.generate( this.getSession() ).vacuumTable();

        this.getSession().clear();
        this.getSession().close();
        this.getSessionFactory().close();
        this.getValidatorFactory().close();

        StandardServiceRegistryBuilder.destroy( this.getRegistry() );

        CONNECTOR = null;

        super.logging( this );
    }
}
