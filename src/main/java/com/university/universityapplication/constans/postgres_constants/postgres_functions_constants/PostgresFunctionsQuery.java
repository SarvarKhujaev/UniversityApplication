package com.university.universityapplication.constans.postgres_constants.postgres_functions_constants;

import com.university.universityapplication.constans.postgres_constants.PostgresCommonCommands;
import com.university.universityapplication.constans.postgres_constants.PostgresCreateValues;
import com.university.universityapplication.constans.postgres_constants.PostgreSqlSchema;

public enum PostgresFunctionsQuery {
    SELECT_ALL_TABLES_IN_SCHEMA;

    public String getQuery () {
        return """
            %s %s() returns SETOF text as $$
            SELECT
                table_schema || '.' || table_name as show_tables
            FROM
                information_schema.tables
            WHERE
                table_type = 'BASE TABLE'
            AND
                table_schema = '%s';
            $$
            language sql;
            """.formatted(
                PostgresCommonCommands.CREATE.formatted(
                        PostgresCreateValues.FUNCTION
                ),
                PostgresFunctionsNames.SHOW_ALL_TABLES_IN_SCHEMA,
                PostgreSqlSchema.UNIVERSITY
        );
    }
}
