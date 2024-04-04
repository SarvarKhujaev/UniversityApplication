package com.university.universityapplication;

import com.university.universityapplication.database.PostgresPartitionsRegister;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.university.universityapplication.database.HibernateConnector;

import java.time.Month;
import java.time.Year;
import java.util.Arrays;
import java.util.Calendar;

@SpringBootApplication
public class UniversityApplication {
    public static ApplicationContext context;

    public static void main( final String[] args ) {
//        context = SpringApplication.run( UniversityApplication.class, args );

//        HibernateConnector.getInstance().insertStudents();
//        HibernateConnector.getInstance().insertTeachers();
//        HibernateConnector.getInstance().saveComments();
//        HibernateConnector.getInstance().testPreparedStatement();
//        HibernateConnector.getInstance().close();

        new PostgresPartitionsRegister().registerPartitionsForCurrentYear();

//        final Calendar calendar = Calendar.getInstance();
//
//        calendar.set( Calendar.YEAR, Year.now().getValue() );
//        calendar.set( Calendar.DAY_OF_MONTH, 1 );
//
//        Arrays.stream( Month.values() )
//                .forEach( month -> {
//                    calendar.set( Calendar.MONTH, month.getValue() - 1 );
//                    System.out.println( calendar.getTime() );
//
//                    calendar.set( Calendar.MONTH, month.getValue() );
//                    System.out.println( calendar.getTime() );
//                } );

    }
}
