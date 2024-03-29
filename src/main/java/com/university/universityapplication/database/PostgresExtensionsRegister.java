package com.university.universityapplication.database;

import com.university.universityapplication.interfaces.PostgresExtensionsRegisterInterface;
import com.university.universityapplication.inspectors.LogInspector;

import org.hibernate.Session;

public final class PostgresExtensionsRegister extends LogInspector implements PostgresExtensionsRegisterInterface {
    private final Session session;

    private Session getSession() {
        return this.session;
    }

    public static void generate (
            final Session session
    ) {
        new PostgresExtensionsRegister( session );
    }

    private PostgresExtensionsRegister (
            final Session session
    ) {
        this.session = session;
        this.createExtension();
    }

    @Override
    public void createExtension() {
        super.analyze(
                super.getPostgresExtensions(),
                extension -> super.logging(
                        this.getSession().createNativeQuery(
                                extension
                        ).getQueryString()
                )
        );
    }
}
