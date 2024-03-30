package com.university.universityapplication.database;

import com.university.universityapplication.interfaces.PostgresStatisticsTableInterface;
import com.university.universityapplication.inspectors.LogInspector;

import org.hibernate.Session;

/*
создает все таблицы для сбора статистики по всем основным таблицам
*/
public final class PostgresStatisticsTableRegister extends LogInspector implements PostgresStatisticsTableInterface {
    private final Session session;

    private Session getSession() {
        return this.session;
    }

    public static void generate (
            final Session session
    ) {
        new PostgresStatisticsTableRegister( session );
    }

    private PostgresStatisticsTableRegister (
            final Session session
    ) {
        this.session = session;

        this.createAllTablesForStats();
    }

    @Override
    public void createAllTablesForStats() {
        super.analyze(
                super.getTablesForStatsNames(),
                tableName -> super.logging(
                        this.getSession().createNativeQuery(
                                tableName
                        ).getQueryString()
                )
        );
    }
}
