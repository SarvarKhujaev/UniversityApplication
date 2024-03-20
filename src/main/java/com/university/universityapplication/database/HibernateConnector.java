package com.university.universityapplication.database;

import com.university.universityapplication.interfaces.ServiceCommonMethods;
import com.university.universityapplication.constans.PostgreSqlTables;
import com.university.universityapplication.inspectors.Archive;
import com.university.universityapplication.entities.*;

import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.stat.CacheRegionStatistics;
import org.hibernate.boot.MetadataSources;
import org.hibernate.*;

import jakarta.validation.ValidatorFactory;
import jakarta.validation.Validation;

import java.text.MessageFormat;

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

    private static HibernateConnector connector = new HibernateConnector();

    public static HibernateConnector getInstance() {
        return connector != null ? connector : ( connector = new HibernateConnector() );
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

        /*
        Hibernate specific JDBC batch size configuration on a per-Session basis
         */
        this.getSession().setJdbcBatchSize( super.BATCH_SIZE );

        super.logging( this.getClass() );
    }

    private void checkBatchLimit (
            final int operationsCounter
    ) {
        /*
        проверяем что количество операций не превысило
        макс количество Batch
         */
        if ( operationsCounter > 0 && ( operationsCounter & super.BATCH_SIZE ) == 0 ) {
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

        connector = null;

        super.logging( this );
    }
}
