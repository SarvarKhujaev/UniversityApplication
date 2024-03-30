package com.university.universityapplication.database;

import com.university.universityapplication.entities.query_result_mapper_entities.BufferAnalyzeResultMapper;
import com.university.universityapplication.constans.postgres_constants.PostgresBufferMethods;
import com.university.universityapplication.constans.postgres_constants.PostgreSqlSchema;
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
        this.calculateBufferAnalyze();
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
    public void calculateBufferAnalyze () {
        final Transaction transaction = this.getSession().beginTransaction();

        super.analyze(
                super.getTablesList(),
                table -> super.analyze(
                        this.getSession().createNativeQuery(
                                PostgresBufferMethods.SELECT_BUFFER_ANALYZE_FOR_TABLE.formatted(
                                        PostgreSqlSchema.UNIVERSITY,
                                        table
                                ),
                                BufferAnalyzeResultMapper.class
                            )
                            .addScalar( "bufferid", Long.class )
                            .addScalar( "usagecount", Long.class )
                            .addScalar( "reldatabase", Long.class )
                            .addScalar( "relfilenode", Long.class )
                            .addScalar( "reltablespace", Long.class )
                            .addScalar( "relforknumber", Long.class )
                            .addScalar( "relblocknumber", Long.class )
                            .addScalar( "pinning_backends", Long.class )
                            .addScalar( "isdirty", Character.class )
                            .getResultList(),
                        bufferAnalyzeResultMapper -> super.logging( bufferAnalyzeResultMapper.toString() )
                )
        );

        transaction.commit();
        super.logging( transaction );
    }

    @Override
    public void insertTableContentToBuffer () {
        final Transaction transaction = this.getSession().beginTransaction();

        /*
        создаем расширение, меняем настройки pg_config и перезапускаем БД
        */
        super.logging(
                PostgresBufferMethods.SOFT_RELOAD_OF_CONFIGURATIONS
                        + " : "
                        + this.getSession().createNativeQuery(
                                PostgresBufferMethods.SOFT_RELOAD_OF_CONFIGURATIONS
                ).getQueryString()
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
