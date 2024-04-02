package com.university.universityapplication.database;

import com.university.universityapplication.constans.postgres_constants.postgres_statistics_constants.PostgresStatisticsQueries;
import com.university.universityapplication.interfaces.PostgresStatisticsQueryInterface;
import com.university.universityapplication.entities.postgres_stats_entities.PGStats;
import com.university.universityapplication.inspectors.LogInspector;

import org.hibernate.stat.CacheRegionStatistics;
import org.hibernate.Session;

/*
выолняет все запросы связанные со статистикой
*/
public final class PostgresStatisticsQueryController extends LogInspector implements PostgresStatisticsQueryInterface {
    private final Session session;

    private Session getSession() {
        return this.session;
    }

    public static void generate (
            final Session session
    ) {
        new PostgresStatisticsQueryController( session );
    }

    private PostgresStatisticsQueryController (
            final Session session
    ) {
        this.session = session;

        this.get_pg_stats();
    }

    @Override
    public void get_pg_stats() {
        super.analyze(
                super.getTablesList(),
                tableName -> super.analyze(
                        this.getSession().createNativeQuery(
                                PostgresStatisticsQueries.PG_STATS_QUERY.formatted(),
                                PGStats.class
                        ).getResultList(),
                        pgStats -> super.logging(
                                pgStats.getSchemaname()
                        )
                )
        );
    }

    @Override
    public void get_pg_stat_activity() {
        this.getSession().createNativeQuery(
                PostgresStatisticsQueries.PG_STAT_ACTIVITY_QUERY
        );
    }

    @Override
    public void get_pg_stat_database() {
        this.getSession().createNativeQuery(
                PostgresStatisticsQueries.PG_STAT_DATABASE_QUERY
        );
    }

    @Override
    public void get_pg_statistics_ext() {
        this.getSession().createNativeQuery(
                PostgresStatisticsQueries.PG_STATISTICS_EXT_QUERY
        );
    }

    @Override
    public void get_pg_stat_statements() {
        this.getSession().createNativeQuery(
                PostgresStatisticsQueries.PG_STAT_STATEMENTS_QUERY
        );
    }

    @Override
    public void get_pg_stat_user_tables() {
        this.getSession().createNativeQuery(
                PostgresStatisticsQueries.PG_STAT_USER_TABLES_QUERY
        );
    }

    @Override
    public void get_pg_stat_user_indexes() {
        this.getSession().createNativeQuery(
                PostgresStatisticsQueries.PG_STAT_USER_INDEXES_QUERY
        );
    }

    @Override
    public void get_pg_prepared_statements() {
        this.getSession().createNativeQuery(
                PostgresStatisticsQueries.PG_PREPARED_STATEMENTS_QUERY
        );
    }

    /*
    If you enable the hibernate.generate_statistics configuration property,
    Hibernate will expose a number of metrics via SessionFactory.getStatistics().
    Hibernate can even be configured to expose these statistics via JMX.

    This way, you can get access to the Statistics class which comprises all sort of second-level cache metrics.
    */
    @Override
    public void readCacheStatistics () {
        super.analyze(
                super.getAllCacheRegionsNames(),
                regionName -> {
                    final CacheRegionStatistics regionStatistics = this.getSession()
                            .getSessionFactory()
                            .getStatistics()
                            .getDomainDataRegionStatistics( regionName );

                    super.logging(
                            regionStatistics.getRegionName()
                    );

                    super.logging(
                            regionStatistics.getHitCount()
                    );

                    super.logging(
                            regionStatistics.getMissCount()
                    );

                    super.logging(
                            regionStatistics.getSizeInMemory()
                    );
                }
        );
    }
}
