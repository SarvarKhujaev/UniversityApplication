package com.university.universityapplication.constans.postgres_constants.postgres_index_constants;

import com.university.universityapplication.constans.postgres_constants.PostgreSqlSchema;
import com.university.universityapplication.constans.postgres_constants.PostgreSqlTables;

/*
хранит все QUERY запросы для создания индексов
*/
public final class PostgresIndexesCreateQueries {
    public static final String UNIVERSITY_TEACHER_FIO_INDEX = String.join(
            " ",
            PostgresIndexParams.CREATE_INDEX.formatted(
                    PostgresIndexesNames.UNIVERSITY_TEACHERS_NAME_INDEX.name(),

                    PostgreSqlSchema.UNIVERSITY,
                    PostgreSqlTables.TEACHERS,

                    "name %s".formatted(
                            PostgresIndexParams.ASC
                    )
            ),
            PostgresIndexParams.INCLUDE.formatted(
                    "surname ASC, father_name ASC"
            ),
            PostgresIndexes.BTREE,
            ";"
    );

    public static final String UNIVERSITY_TEACHER_EMAIL_INDEX = String.join(
            " ",
            PostgresIndexParams.CREATE_INDEX.formatted(
                    PostgresIndexesNames.UNIVERSITY_TEACHERS_EMAIL_INDEX.name(),

                    PostgreSqlSchema.UNIVERSITY,
                    PostgreSqlTables.TEACHERS,

                    "email %s".formatted(
                            PostgresIndexParams.ASC
                    )
            ),
            PostgresIndexes.BTREE,
            ";"
    );

    public static final String UNIVERSITY_STUDENT_FIO_INDEX = String.join(
            " ",
            PostgresIndexParams.CREATE_INDEX.formatted(
                    PostgresIndexesNames.UNIVERSITY_STUDENTS_NAME_INDEX.name(),

                    PostgreSqlSchema.UNIVERSITY,
                    PostgreSqlTables.STUDENTS,

                    "name %s".formatted(
                            PostgresIndexParams.ASC
                    )
            ),
            PostgresIndexParams.INCLUDE.formatted(
                    "surname ASC, father_name ASC"
            ),
            PostgresIndexes.BTREE,
            ";"
    );

    public static final String UNIVERSITY_STUDENT_EMAIL_INDEX = String.join(
            " ",
            PostgresIndexParams.CREATE_INDEX.formatted(
                    PostgresIndexesNames.UNIVERSITY_STUDENTS_EMAIL_INDEX.name(),

                    PostgreSqlSchema.UNIVERSITY,
                    PostgreSqlTables.STUDENTS,

                    "email %s".formatted(
                            PostgresIndexParams.ASC
                    )
            ),
            PostgresIndexes.BTREE,
            ";"
    );

    public static final String UNIVERSITY_GROUPS_GROUP_NAME_INDEX = String.join(
            " ",
            PostgresIndexParams.CREATE_INDEX.formatted(
                    PostgresIndexesNames.UNIVERSITY_GROUPS_GROUP_NAME_INDEX.name(),

                    PostgreSqlSchema.UNIVERSITY,
                    PostgreSqlTables.GROUPS,

                    "group_name %s".formatted(
                            PostgresIndexParams.ASC
                    )
            ),
            PostgresIndexes.BTREE,
            ";"
    );

    public static final String UNIVERSITY_COMMENT_MARK_INDEX = String.join(
            " ",
            PostgresIndexParams.CREATE_INDEX.formatted(
                    PostgresIndexesNames.UNIVERSITY_COMMENT_MARK_INDEX.name(),

                    PostgreSqlSchema.UNIVERSITY,
                    PostgreSqlTables.COMMENTS,

                    "mark %s".formatted(
                            PostgresIndexParams.ASC
                    )
            ),
            PostgresIndexes.BTREE,
            ";"
    );

    public static final String UNIVERSITY_LESSON_LESSON_NAME_INDEX = String.join(
            " ",
            PostgresIndexParams.CREATE_INDEX.formatted(
                    PostgresIndexesNames.UNIVERSITY_LESSON_LESSON_NAME_INDEX.name(),

                    PostgreSqlSchema.UNIVERSITY,
                    PostgreSqlTables.LESSONS,

                    "lesson_name %s".formatted(
                            PostgresIndexParams.ASC
                    )
            ),
            PostgresIndexes.BTREE,
            ";"
    );

    public static final String UNIVERSITY_LESSON_LESSON_STATUS_INDEX = String.join(
            " ",
            PostgresIndexParams.CREATE_INDEX.formatted(
                    PostgresIndexesNames.UNIVERSITY_LESSON_LESSON_STATUS_INDEX.name(),

                    PostgreSqlSchema.UNIVERSITY,
                    PostgreSqlTables.LESSONS,

                    "lesson_status %s".formatted(
                            PostgresIndexParams.ASC
                    )
            ),
            PostgresIndexes.BTREE,
            ";"
    );

    public static final String UNIVERSITY_STUDENT_APPEARANCE_IN_LESSONS_TYPES_INDEX = String.join(
            " ",
            PostgresIndexParams.CREATE_INDEX.formatted(
                    PostgresIndexesNames.UNIVERSITY_STUDENT_APPEARANCE_IN_LESSONS_TYPES_INDEX.name(),

                    PostgreSqlSchema.UNIVERSITY,
                    PostgreSqlTables.STUDENT_APPEARANCE_IN_LESSONS,

                    "lesson_appearance_types %s".formatted(
                            PostgresIndexParams.ASC
                    )
            ),
            PostgresIndexes.BTREE,
            ";"
    );
}
