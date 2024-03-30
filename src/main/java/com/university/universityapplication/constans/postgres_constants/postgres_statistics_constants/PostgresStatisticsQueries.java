package com.university.universityapplication.constans.postgres_constants.postgres_statistics_constants;

import com.university.universityapplication.constans.postgres_constants.PostgreSqlSchema;
import com.university.universityapplication.constans.postgres_constants.PostgresCommonCommands;
import com.university.universityapplication.constans.postgres_constants.PostgreSqlTables;

/*
хранит все запросы связанные со статистикой
 */
public final class PostgresStatisticsQueries {
    /*
    SELECT * FROM pg_stat_database WHERE datname = 'demo';

    Здесь видим, сколько данных было выбрано из БД, сколько было вставок,
    обновлений, были ли дедлоки и многое другое.
    Необходимо заметить, что информация в статистику попадает с
    минимальным лагом, и если мы запускаем выборку из этого представления в
    рамках транзакции, то данные будут действительны на момент начала
    транзакции, так как уровень изоляции транзакций у нас read committed
    */
    public static final String PG_STAT_DATABASE_QUERY = String.join(
            " ",
            PostgresCommonCommands.SELECT_FROM,
            PostgresStatisticsParams.PG_STAT_DATABASE,
            "WHERE datname = '%s'"
    );

    /*
    SELECT * FROM pg_stats WHERE tablename = 'flights'
    */
    public static final String PG_STATS_QUERY = String.join(
            " ",
            PostgresCommonCommands.SELECT_FROM,
            PostgresStatisticsParams.PG_STATS,
            "WHERE tablename = '%s'"
    );

    public static final String PG_STATISTICS_EXT_QUERY = String.join(
            " ",
            PostgresCommonCommands.SELECT_FROM,
            PostgresStatisticsParams.PG_STATISTICS_EXT
    );

    /*
    SELECT * FROM pg_stat_activity;
    */
    public static final String PG_STAT_ACTIVITY_QUERY = String.join(
            " ",
            PostgresCommonCommands.SELECT_FROM,
            PostgresStatisticsParams.PG_STAT_ACTIVITY
    );

    /*
    SELECT * FROM pg_stat_user_tables WHERE relname = 'flights'
    */
    public static final String PG_STAT_USER_TABLES_QUERY = String.join(
            " ",
            PostgresCommonCommands.SELECT_FROM,
            PostgresStatisticsParams.PG_STAT_USER_TABLES,
            "WHERE relname = '%s'"
    );

    public static final String PG_STAT_USER_INDEXES_QUERY = String.join(
            " ",
            PostgresCommonCommands.SELECT_FROM,
            PostgresStatisticsParams.PG_STAT_USER_INDEXES
    );

    public static final String PG_STAT_STATEMENTS_QUERY = String.join(
            " ",
            PostgresCommonCommands.SELECT_FROM,
            PostgresStatisticsParams.PG_STAT_STATEMENTS
    );

    public static final String CREATE_UNIVERSITY_TEACHERS_TABLE_STATISTICS_QUERY = String.join(
            " ",
            PostgresStatisticsParams.CREATE_STATISTICS.formatted(
                    PostgresStatisticsNames.UNIVERSITY_TEACHERS_TABLE_STATISTICS.name(),

                    PostgresStatisticsTypes.DEPENDENCIES,
                    "name, email",

                    PostgreSqlSchema.UNIVERSITY,
                    PostgreSqlTables.TEACHERS
            )
    );

    public static final String CREATE_UNIVERSITY_STUDENTS_TABLE_STATISTICS_QUERY = String.join(
            " ",
            PostgresStatisticsParams.CREATE_STATISTICS.formatted(
                    PostgresStatisticsNames.UNIVERSITY_STUDENTS_TABLE_STATISTICS.name(),

                    PostgresStatisticsTypes.DEPENDENCIES,
                    "name, email",

                    PostgreSqlSchema.UNIVERSITY,
                    PostgreSqlTables.STUDENTS
            )
    );

    public static final String CREATE_UNIVERSITY_LESSON_TABLE_STATISTICS_QUERY =
            PostgresStatisticsParams.CREATE_STATISTICS.formatted(
                    PostgresStatisticsNames.UNIVERSITY_LESSON_TABLE_STATISTICS.name(),

                    PostgresStatisticsTypes.DEPENDENCIES,
                    "lesson_status, lessonName",

                    PostgreSqlSchema.UNIVERSITY,
                    PostgreSqlTables.LESSONS
            );

    public static final String CREATE_UNIVERSITY_GROUPS_TABLE_STATISTICS_QUERY = String.join(
            " ",
            PostgresStatisticsParams.CREATE_STATISTICS.formatted(
                    PostgresStatisticsNames.UNIVERSITY_GROUPS_TABLE_STATISTICS.name(),

                    PostgresStatisticsTypes.DEPENDENCIES,
                    "group_name, students_number",

                    PostgreSqlSchema.UNIVERSITY,
                    PostgreSqlTables.GROUPS
            )
    );

    public static final String CREATE_UNIVERSITY_STUDENT_APPEARANCE_TABLE_STATISTICS_QUERY = String.join(
            " ",
            PostgresStatisticsParams.CREATE_STATISTICS.formatted(
                    PostgresStatisticsNames.UNIVERSITY_STUDENT_APPEARANCE_TABLE_STATISTICS.name(),

                    PostgresStatisticsTypes.DEPENDENCIES,
                    "lesson_appearance_types",

                    PostgreSqlSchema.UNIVERSITY,
                    PostgreSqlTables.STUDENT_APPEARANCE_IN_LESSONS
            )
    );
}
