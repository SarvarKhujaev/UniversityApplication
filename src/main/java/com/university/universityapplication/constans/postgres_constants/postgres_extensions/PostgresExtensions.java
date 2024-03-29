package com.university.universityapplication.constans.postgres_constants.postgres_extensions;

public final class PostgresExtensions {
    public static final String CREATE_EXTENSION_PG_PREWARM = "CREATE EXTENSION IF NOT EXISTS pg_prewarm;";

    /*
    создает расширение для работы и аналитики буфера кэширования
    */
    public static final String CREATE_EXTENSION_FOR_BUFFER_READ = "CREATE EXTENSION IF NOT EXISTS pg_buffercache;";

    public static final String CREATE_EXTENSION_PG_STAT_STATEMENTS = "CREATE EXTENSION IF NOT EXISTS pg_stat_statements;";
}
