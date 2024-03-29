package com.university.universityapplication.constans.postgres_constants.postgres_constraints_constants;

public final class PostgresConstraintsValues {
    public static final String GROUP_TABLE_CONSTRAINT_VALUE = """
                max_students_number >= 3 AND students_number >= 0 AND students_number < max_students_number
                """;

    public static final String COMMENT_TABLE_CONSTRAINT_VALUE = """
                mark >= 1 AND mark <= 5
                """;

    public static final String TEACHER_TABLE_CONSTRAINT_VALUE = """
                age >= 18 AND age <= 80
                """;

    public static final String PHONE_NUMBER_CONSTRAINT_VALUE = """
            character_length( phone_number ) = 13
            AND substr( phone_number, 0, 6 ) = '+9989'
            """;
}
