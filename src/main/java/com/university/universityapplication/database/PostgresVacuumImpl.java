package com.university.universityapplication.database;

import com.university.universityapplication.constans.postgres_constants.PostgresVacuumMethods;
import com.university.universityapplication.constans.postgres_constants.PostgreSqlSchema;
import com.university.universityapplication.interfaces.PostgresVacuumMethodsInterface;
import com.university.universityapplication.inspectors.LogInspector;

import java.text.MessageFormat;
import org.hibernate.Session;

/*
работает с инструментом VACUUM PostgreSQL
*/
public final class PostgresVacuumImpl extends LogInspector implements PostgresVacuumMethodsInterface {
    private final Session session;

    private Session getSession() {
        return this.session;
    }

    public static void generate (
            final Session session
    ) {
        new PostgresVacuumImpl( session );
    }

    private PostgresVacuumImpl (
            final Session session
    ) {
        this.session = session;
        this.vacuumTable();
    }

    @Override
    public void vacuumTable () {
        super.analyze(
                super.getTablesList(),
                table -> super.logging(
                        table
                                + " was cleaned: "
                                + this.getSession().createNativeQuery(
                                MessageFormat.format(
                                        """
                                        VACUUM( {0}, {1} ) {2}.{3}
                                        """,
                                        PostgresVacuumMethods.ANALYZE,
                                        PostgresVacuumMethods.VERBOSE,

                                        PostgreSqlSchema.UNIVERSITY,
                                        table
                                )
                        ).getQueryString()
                )
        );
    }
}
