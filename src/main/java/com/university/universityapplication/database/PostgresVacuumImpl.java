package com.university.universityapplication.database;

import com.university.universityapplication.constans.postgres_constants.PostgresVacuumMethods;
import com.university.universityapplication.interfaces.PostgresVacuumMethodsInterface;
import com.university.universityapplication.inspectors.LogInspector;

import java.text.MessageFormat;

import org.hibernate.Transaction;
import org.hibernate.Session;

/*
работает с инструментом VACUUM PostgreSQL
*/
public final class PostgresVacuumImpl extends LogInspector implements PostgresVacuumMethodsInterface {
    private final Session session;

    private Session getSession() {
        return this.session;
    }

    public static PostgresVacuumImpl generate (
            final Session session
    ) {
        return new PostgresVacuumImpl( session );
    }

    private PostgresVacuumImpl (
            final Session session
    ) {
        this.session = session;
    }

    /*
    проводит очистку VACUUM всех таблиц
    */
    @Override
    public void vacuumTable () {
        final Transaction transaction = this.getSession().beginTransaction();

        super.analyze(
                PostgresFunctionsRegister.generate( this.getSession() ).getListOfDbTables(),
                schemaAndTableName -> super.logging(
                        schemaAndTableName
                        + " was cleaned: "
                        + this.getSession().createNativeQuery(
                                MessageFormat.format(
                                        """
                                                {0}( {1}, {2} ) {3}
                                        """,
                                        PostgresVacuumMethods.VACUUM,

                                        PostgresVacuumMethods.ANALYZE,
                                        PostgresVacuumMethods.VERBOSE,

                                        schemaAndTableName
                                )
                        ).getQueryString()
                )
        );

        PostgresMaterializedViewRegister.generate( this.getSession() ).refreshAllViews();
        PostgresIndexesRegister.generate( this.getSession() ).reIndex();
        PostgresCheckpointRegister.generate( this.getSession() );

        transaction.commit();
        super.logging( transaction );
    }

    /*
    провеодит очистку конкретной таблицы
    нужно выбрать тип очистки ( Full, Verbose ) и название таблицы
    */
    @Override
    public void vacuumTable (
            final String tableName,
            final PostgresVacuumMethods vacuumMethod
    ) {
        super.logging(
                this.getSession().createNativeQuery(
                        MessageFormat.format(
                                "{0} {1} {2}",
                                PostgresVacuumMethods.VACUUM,
                                vacuumMethod,
                                tableName
                        )
                ).getQueryString()
        );
    }
}
