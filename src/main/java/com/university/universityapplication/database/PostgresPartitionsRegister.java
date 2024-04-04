package com.university.universityapplication.database;

import com.university.universityapplication.constans.postgres_constants.PostgresCommonCommands;
import com.university.universityapplication.constans.postgres_constants.PostgresCreateValues;
import com.university.universityapplication.constans.postgres_constants.PostgreSqlSchema;
import com.university.universityapplication.inspectors.LogInspector;

import org.hibernate.Session;

public final class PostgresPartitionsRegister extends LogInspector {
    private Session session;

    private Session getSession() {
        return this.session;
    }

    public static void generate (
            final Session session
    ) {
        new PostgresPartitionsRegister( session );
    }

    public PostgresPartitionsRegister () {}

    private PostgresPartitionsRegister (
            final Session session
    ) {
        this.session = session;
    }

    public void registerPartitionsForCurrentYear () {
        /*
        определяем текущий год
        */
        final int year = super.getCurrentYear();

        super.analyze(
                super.getPartitionsTablesList(), // берем название нужных таблиц
                tableName -> super.analyze(
                        super.getListOfMonths(),
                        month -> {
                            final String temp = String.join(
                                    " ",
                                    PostgresCommonCommands.CREATE.formatted(
                                            PostgresCreateValues.TABLE
                                    ),
                                    PostgresCommonCommands.RANGE_PARTITIONING.formatted(
                                            super.getYearAndMonthConvertedValue(
                                                    tableName,
                                                    month,
                                                    year
                                            ),

                                            PostgreSqlSchema.UNIVERSITY,
                                            tableName,

                                            super.getMonthStartOrEnd(
                                                    true,
                                                    month
                                            ),

                                            super.getMonthStartOrEnd(
                                                    false,
                                                    month
                                            )
                                    )
                            );

                            System.out.println( temp );
                        }
                )
        );
    }
}
