package com.university.universityapplication.interfaces;

import com.university.universityapplication.constans.postgres_constants.PostgresVacuumMethods;

public interface PostgresVacuumMethodsInterface {
    /*
    очищаем таблицу от старых и не используемых записей
    */
    void vacuumTable ();

    void vacuumTable (
            final String tableName,
            final PostgresVacuumMethods vacuumMethod
    );
}
