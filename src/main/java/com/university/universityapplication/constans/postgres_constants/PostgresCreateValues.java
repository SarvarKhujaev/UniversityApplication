package com.university.universityapplication.constans.postgres_constants;

/*
хранит все типы данных или параметров которые используется при команде CREATE
*/
public enum PostgresCreateValues {
    MATERIALIZED_VIEW,
    STATISTICS,
    EXTENSION,
    FUNCTION,
    TABLE,
    INDEX,
    ENUM;

    public String getOriginalValue () {
        return "MATERIALIZED VIEW";
    }
}
