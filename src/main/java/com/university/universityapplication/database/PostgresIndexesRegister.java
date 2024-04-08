package com.university.universityapplication.database;

import com.university.universityapplication.constans.postgres_constants.postgres_index_constants.PostgresIndexesNames;
import com.university.universityapplication.constans.postgres_constants.postgres_index_constants.PostgresIndexParams;
import com.university.universityapplication.inspectors.LogInspector;

import org.hibernate.Session;

/*
регистрирует все индексы по всем таблицам
*/
public final class PostgresIndexesRegister extends LogInspector {
    private final Session session;

    private Session getSession() {
        return this.session;
    }

    public static PostgresIndexesRegister generate (
            final Session session
    ) {
        return new PostgresIndexesRegister( session );
    }

    private PostgresIndexesRegister (
            final Session session
    ) {
        this.session = session;
    }

    /*
    создает индексы
    */
    public void createIndex() {
        super.analyze(
                super.getIndexCreateQueries(),
                indexQuery -> super.logging(
                        this.getSession().createNativeQuery(
                                indexQuery
                        ).getQueryString()
                )
        );
    }

    /*
    применяет Reindex на все индексы в таблицах
    для очищения индексов от пустых и не используемых значений
    */
    public void reIndex() {
        super.analyze(
                super.newList( PostgresIndexesNames.values() ),
                postgresIndexesName -> super.logging(
                        this.getSession().createNativeQuery(
                                PostgresIndexParams.REINDEX.formatted(
                                        postgresIndexesName
                                )
                        ).getQueryString()
                )
        );
    }

    public void reIndex (
            final String indexName
    ) {
        super.logging(
                this.getSession().createNativeQuery(
                        PostgresIndexParams.REINDEX.formatted(
                                indexName
                        )
                ).getQueryString()
        );
    }
}
