package com.university.universityapplication.database;

import com.university.universityapplication.constans.postgres_constants.PostgreDataTypes;
import com.university.universityapplication.constans.postgres_constants.postgres_prepared_constants.PostgresPreparedQueries;
import com.university.universityapplication.constans.postgres_constants.postgres_prepared_constants.PostgresPreparedQueryNames;
import com.university.universityapplication.constans.postgres_constants.postgres_prepared_constants.PostgresPreparedQueryParams;
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
                query -> this.getSession().createNativeQuery( query )
        );
    }
}
