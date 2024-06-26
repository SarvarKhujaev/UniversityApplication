package com.university.universityapplication.inspectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.hibernate.Transaction;
import org.junit.runner.Result;

public class LogInspector extends TimeInspector {
    public LogInspector() {}

    public LogInspector (
            final Result result
    ) {
        super.analyze(
                result.getFailures(),
                this::logging
        );

        this.logging( "Testing has ended successfully: " + result.wasSuccessful() );
    }

    private final Logger LOGGER = LogManager.getLogger( "LOGGER_WITH_JSON_LAYOUT" );

    private Logger getLOGGER () {
        return this.LOGGER;
    }

    protected final void logging ( final Class<?> clazz ) {
        this.getLOGGER().info( clazz.getName() + " was created at: " + super.newDate() );
    }

    protected final void logging ( final Object o ) {
        this.getLOGGER().info( o.getClass().getName() + " was closed successfully at: " + super.newDate() );
    }

    protected final void logging ( final String message ) {
        this.getLOGGER().info( message );
    }

    protected final void logging ( final Transaction transaction ) {
        this.getLOGGER().info( transaction.getStatus() );
    }
}
