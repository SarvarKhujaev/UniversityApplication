package com.university.universityapplication.database;

import com.university.universityapplication.constans.postgres_constants.postgres_functions_constants.PostgresFunctionsQuery;
import com.university.universityapplication.interfaces.PostgresFunctionsRegisterInterface;
import com.university.universityapplication.inspectors.LogInspector;

import org.hibernate.Session;

public final class PostgresFunctionsRegister extends LogInspector implements PostgresFunctionsRegisterInterface {
    private final Session session;

    private Session getSession() {
        return this.session;
    }

    public static void generate (
            final Session session
    ) {
        new PostgresFunctionsRegister( session );
    }

    private PostgresFunctionsRegister(
            final Session session
    ) {
        this.session = session;
    }

    @Override
    public void createAllFunctions () {
        super.analyze(
                super.newList( PostgresFunctionsQuery.values() ),
                postgresFunctionsQuery -> super.logging(
                        this.getSession().createNativeQuery(
                                postgresFunctionsQuery.getQuery()
                        ).getQueryString()
                )
        );
    }
}
