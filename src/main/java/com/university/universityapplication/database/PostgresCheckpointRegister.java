package com.university.universityapplication.database;

import com.university.universityapplication.constans.postgres_constants.PostgresWalOperations;
import com.university.universityapplication.interfaces.PostgresCheckpointMethodsInterface;
import com.university.universityapplication.inspectors.LogInspector;

import com.university.universityapplication.interfaces.ServiceCommonMethods;
import org.hibernate.Session;

/*
отвечает за выполнение команды CHECKPOINT
*/
public final class PostgresCheckpointRegister extends LogInspector implements PostgresCheckpointMethodsInterface {
    private final Session session;

    private Session getSession() {
        return this.session;
    }

    public static void generate (
            final Session session
    ) {
        new PostgresCheckpointRegister( session );
    }

    private PostgresCheckpointRegister (
            final Session session
    ) {
        this.session = session;

        this.completeCheckpoint();
    }

    @Override
    public void completeCheckpoint() {
        super.logging(
                this.getSession().createNativeQuery(
                        PostgresWalOperations.CHECKPOINT
                ).getQueryString()
        );
    }
}
