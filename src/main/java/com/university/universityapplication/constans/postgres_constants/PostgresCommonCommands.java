package com.university.universityapplication.constans.postgres_constants;

/*
хранит все основные и часто используемые запрос в БД
*/
public final class PostgresCommonCommands {
    public static final String SELECT_FROM = "SELECT * FROM";

    public static final String CREATE = "CREATE %s IF NOT EXISTS";

    public static final String CREATE_OR_REPLACE = "CREATE OR REPLACE %s IF NOT EXISTS";

    public static final String SELECT_ALL_FROM = "SELECT * FROM %s.%s;";
}
