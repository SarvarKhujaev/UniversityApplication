package com.university.universityapplication;

import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.university.universityapplication.database.HibernateConnector;

@SpringBootApplication
public class UniversityApplication {
    public static ApplicationContext context;

    public static void main( final String[] args ) {
        context = SpringApplication.run( UniversityApplication.class, args );

        HibernateConnector.getInstance().getFromView();
        HibernateConnector.getInstance().close();
    }
}
