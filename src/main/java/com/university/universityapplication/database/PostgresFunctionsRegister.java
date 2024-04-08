package com.university.universityapplication.database;

import com.university.universityapplication.constans.postgres_constants.postgres_functions_constants.PostgresFunctionsNames;
import com.university.universityapplication.constans.postgres_constants.postgres_functions_constants.PostgresFunctionsQuery;
import com.university.universityapplication.constans.postgres_constants.PostgresCommonCommands;
import com.university.universityapplication.interfaces.PostgresFunctionsRegisterInterface;
import com.university.universityapplication.inspectors.LogInspector;

import org.hibernate.Session;
import java.util.List;

public final class PostgresFunctionsRegister extends LogInspector implements PostgresFunctionsRegisterInterface {
    private final Session session;

    private Session getSession() {
        return this.session;
    }

    public static PostgresFunctionsRegister generate (
            final Session session
    ) {
        return new PostgresFunctionsRegister( session );
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

    @Override
    public List< String > getListOfDbTables () {
        return this.getSession().createNativeQuery(
                String.join(
                        " ",
                        PostgresCommonCommands.SELECT_FROM,
                        PostgresFunctionsNames.SHOW_ALL_TABLES_IN_SCHEMA.name(),
                        "();"
                ),
                String.class
        ).getResultList();
    }

    @Override
    public List< String > getListOfIndexesInTable (
            final String schemaName,
            final String tableName
    ) {
        return this.getSession().createNativeQuery(
                String.join(
                        " ",
                        PostgresCommonCommands.SELECT_FROM,
                        PostgresFunctionsNames.SHOW_ALL_INDEXES_IN_TABLE.name(),
                        "( schemaname => %s, tablename => %s );".formatted(
                                schemaName,
                                tableName
                        )
                ),
                String.class
        ).getResultList();
    }
}
