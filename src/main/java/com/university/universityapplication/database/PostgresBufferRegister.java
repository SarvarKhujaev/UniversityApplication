package com.university.universityapplication.database;

import com.university.universityapplication.constans.postgres_constants.PostgresBufferMethods;
import com.university.universityapplication.interfaces.PostgresBufferControlInterface;
import com.university.universityapplication.inspectors.LogInspector;

import org.hibernate.Transaction;
import org.hibernate.Session;

/*
работает с кэщом буферизации PostgreSQL
*/
public final class PostgresBufferRegister extends LogInspector implements PostgresBufferControlInterface {
    private final Session session;

    private Session getSession() {
        return this.session;
    }

    public static void generate (
            final Session session
    ) {
        new PostgresBufferRegister( session );
    }

    private PostgresBufferRegister (
            final Session session
    ) {
        this.session = session;

        this.prewarmTable();
        this.insertTableContentToBuffer();
    }

    @Override
    public void prewarmTable () {
        /*
        прогреваем кэш
        */
        super.logging(
                PostgresBufferMethods.PREWARM_TABLE
                        + " : "
                        + this.getSession().createQuery(
                        PostgresBufferMethods.PREWARM_TABLE
                ).getSingleResult()
        );
    }

    @Override
    public void insertTableContentToBuffer () {
        final Transaction transaction = this.getSession().beginTransaction();

        /*
        создаем расширение, меняем настройки pg_config и перезапускаем БД
        */
        super.logging(
                PostgresBufferMethods.CREATE_EXTENSION_FOR_BUFFER_WARMING
                        + " : "
                        + this.getSession().createQuery(
                        PostgresBufferMethods.CREATE_EXTENSION_FOR_BUFFER_WARMING
                ).getSingleResult()
        );

        /*
        загружаем список таблиц в буферы
        */
        super.analyze(
                super.getTablesList(),
                table -> super.logging(
                        table
                                + " was inserted into buffer: "
                                + this.getSession().createQuery(
                                PostgresBufferMethods.INSERT_TABLE_CONTENT_INTO_BUFFER.formatted(
                                        table
                                )
                        ).getQueryString()
                )
        );

        transaction.commit();
        super.logging( transaction );
    }
}
