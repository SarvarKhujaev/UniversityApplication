package com.university.universityapplication;

import com.university.universityapplication.constans.hibernate.HibernateNativeNamedQueries;
import com.university.universityapplication.constans.postgres_constants.PostgreSqlTables;
import com.university.universityapplication.constans.postgres_constants.PostgreSqlSchema;
import com.university.universityapplication.database.HibernateConnector;
import com.university.universityapplication.entities.Student;
import com.university.universityapplication.entities.Teacher;

import reactor.core.scheduler.Schedulers;
import reactor.core.publisher.Flux;
import junit.framework.TestCase;

public final class DatabaseLoadTest extends TestCase {
    private HibernateConnector connector;

    /*
    запускается при инициализации самого класса
    перед началом самого тестирования
    */
    @Override
    public void setUp () {
        this.connector = HibernateConnector.getInstance();
    }

    /*
    запускается в конце тестиирования
    */
    @Override
    public void tearDown () {
        this.connector.close();
        this.connector = null;
    }

    /*
    проверяет что Hibernate и БД работают
    */
    public void testHibernateConnectionEstablishment () {
        assertNotNull( this.connector );
        assertNotNull( this.connector.getSession() );
        assertNotNull( this.connector.getSessionFactory() );

        assertTrue( this.connector.getSession().isConnected() );
    }

    /*
    проверяет что Hibernate и БД работают
    */
    public void testPostgresLoad () {
        Flux.range( 0, 100 )
                .publishOn( Schedulers.parallel() )
                .parallel( 20 )
                .map( j -> {
                    this.connector.getSession().createNativeQuery(
                            HibernateNativeNamedQueries.GET_STUDENTS_QUERY_TEST.formatted(
                                    PostgreSqlSchema.UNIVERSITY,
                                    PostgreSqlTables.STUDENTS,
                                    "test" + j,
                                    "test" + j,
                                    "test" + j
                            ),
                            Student.class
                    );

                    this.connector.getSession().createNativeQuery(
                            HibernateNativeNamedQueries.GET_STUDENTS_QUERY_TEST.formatted(
                                    PostgreSqlSchema.UNIVERSITY,
                                    PostgreSqlTables.TEACHERS,
                                    "test" + j,
                                    "test" + j,
                                    "test" + j
                            ),
                            Teacher.class
                    );

                    this.connector.getSession().createNativeQuery(
                            HibernateNativeNamedQueries.GET_TEACHER_AVERAGE_MARKS_QUERY
                    );

                    this.connector.getSession().createNativeQuery(
                            HibernateNativeNamedQueries.GET_GROUPED_STUDENTS_STATS_FOR_LESSON_APPEARANCE_QUERY
                    );

                    this.connector.getWithNativeQuery();
                    this.connector.saveComments();
                    for ( int i = 1; i < 6; i++ ) {
                        this.connector.getGroup( i );
                    }

                    return j;
                } )
                .sequential()
                .publishOn( Schedulers.single() )
                .subscribe();
    }
}
