package com.university.universityapplication.constans.postgres_constants.postgres_extensions;

import com.university.universityapplication.constans.postgres_constants.PostgresCommonCommands;
import com.university.universityapplication.constans.postgres_constants.PostgresCreateValues;

public final class PostgresExtensions {
    public static final String CREATE_EXTENSION_PG_PREWARM = String.join(
            " ",
            PostgresCommonCommands.CREATE.formatted(
                    PostgresCreateValues.EXTENSION
            ),
            "pg_prewarm;"
    );

    /*
    создает расширение для работы и аналитики буфера кэширования
    */
    public static final String CREATE_EXTENSION_FOR_BUFFER_READ = String.join(
            " ",
            PostgresCommonCommands.CREATE.formatted(
                    PostgresCreateValues.EXTENSION
            ),
            "pg_buffercache;"
    );

    public static final String CREATE_EXTENSION_PG_STAT_STATEMENTS = String.join(
            " ",
            PostgresCommonCommands.CREATE.formatted(
                    PostgresCreateValues.EXTENSION
            ),
            "pg_stat_statements;"
    );

    /*
    https://medium.com/@mudasirhaji/step-by-step-process-of-how-to-install-timescaledb-with-postgresql-on-aws-ubuntu-ec2-ddc939dd819c

    Создаёт секционированную по времени таблицу, если нужно писать очень
    много данных во времени (системы мониторинга, биржевые системы)
    */
    public static final String CREATE_EXTENSION_TIMESCALE_DB = String.join(
            " ",
            PostgresCommonCommands.CREATE.formatted(
                    PostgresCreateValues.EXTENSION
            ),
            "timescaledb;"
    );
}
