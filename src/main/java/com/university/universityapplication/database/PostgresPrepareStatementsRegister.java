package com.university.universityapplication.database;

import com.university.universityapplication.interfaces.PostgresPrepareStatementsInterface;
import com.university.universityapplication.inspectors.LogInspector;

import org.hibernate.Session;

public final class PostgresPrepareStatementsRegister extends LogInspector implements PostgresPrepareStatementsInterface {
    private final Session session;

    private Session getSession() {
        return this.session;
    }

    public static void generate (
            final Session session
    ) {
        new PostgresPrepareStatementsRegister( session );
    }

    private PostgresPrepareStatementsRegister (
            final Session session
    ) {
        this.session = session;

        this.prepareAllStatements();
    }

    @Override
    public void prepareAllStatements() {
        super.analyze(
                super.getPreparedStatements(),
                query -> {
                    System.out.println(
                            this.getSession().createNativeQuery( query ).getQueryString()
                    );
                }
        );
    }
}
