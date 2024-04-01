package com.university.universityapplication.inspectors;

import com.university.universityapplication.constans.postgres_constants.postgres_prepared_constants.PostgresPreparedQueryParams;
import com.university.universityapplication.constans.postgres_constants.postgres_statistics_constants.PostgresStatisticsQueries;
import com.university.universityapplication.constans.postgres_constants.postgres_index_constants.PostgresIndexesCreateQueries;
import com.university.universityapplication.constans.postgres_constants.postgres_extensions.PostgresExtensions;
import com.university.universityapplication.constans.postgres_constants.PostgreSqlTables;
import com.university.universityapplication.entities.*;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.*;

public class CollectionsInspector extends DataValidateInspector {
    protected CollectionsInspector () {}

    protected final <T> List<T> emptyList () {
        return Collections.emptyList();
    }

    protected final <T> ArrayList<T> newList () {
        return new ArrayList<>();
    }

    protected final <T> List<T> newList (
            final T ... values
    ) {
        return Arrays.asList( values );
    }

    protected final <T, V> Map<T, V> newMap () {
        return new HashMap<>();
    }

    protected final List< String > getTablesList () {
        return List.of(
                PostgreSqlTables.GROUPS,
                PostgreSqlTables.LESSONS,
                PostgreSqlTables.TEACHERS,
                PostgreSqlTables.COMMENTS,
                PostgreSqlTables.STUDENTS,
                PostgreSqlTables.HOMEWORK,
                PostgreSqlTables.STUDENT_MARKS,
                PostgreSqlTables.EDUCATION_DIRECTIONS
        );
    }

    protected final List< String > getPostgresExtensions () {
        return List.of(
                PostgresExtensions.CREATE_EXTENSION_PG_PREWARM,
                PostgresExtensions.CREATE_EXTENSION_FOR_BUFFER_READ,
                PostgresExtensions.CREATE_EXTENSION_PG_STAT_STATEMENTS
        );
    }

    protected final List< Class<?> > getClassesReferences () {
        return List.of(
                Group.class,
                Lesson.class,
                Teacher.class,
                Student.class,
                StudentMarks.class,
                EducationDirection.class,
                StudentAppearanceInLessons.class,
                Comment.class,
                Homework.class
        );
    }

    protected final List< String > getIndexCreateQueries () {
        return List.of(
                PostgresIndexesCreateQueries.UNIVERSITY_TEACHER_FIO_INDEX,
                PostgresIndexesCreateQueries.UNIVERSITY_TEACHER_EMAIL_INDEX,

                PostgresIndexesCreateQueries.UNIVERSITY_STUDENT_FIO_INDEX,
                PostgresIndexesCreateQueries.UNIVERSITY_STUDENT_EMAIL_INDEX,

                PostgresIndexesCreateQueries.UNIVERSITY_GROUPS_GROUP_NAME_INDEX,

                PostgresIndexesCreateQueries.UNIVERSITY_COMMENT_MARK_INDEX,

                PostgresIndexesCreateQueries.UNIVERSITY_LESSON_LESSON_NAME_INDEX,
                PostgresIndexesCreateQueries.UNIVERSITY_LESSON_LESSON_STATUS_INDEX,

                PostgresIndexesCreateQueries.UNIVERSITY_STUDENT_APPEARANCE_IN_LESSONS_TYPES_INDEX
        );
    }

    protected final List< String > getTablesForStatsNames () {
        return List.of(
                PostgresStatisticsQueries.CREATE_UNIVERSITY_LESSON_TABLE_STATISTICS_QUERY,
                PostgresStatisticsQueries.CREATE_UNIVERSITY_GROUPS_TABLE_STATISTICS_QUERY,
                PostgresStatisticsQueries.CREATE_UNIVERSITY_TEACHERS_TABLE_STATISTICS_QUERY,
                PostgresStatisticsQueries.CREATE_UNIVERSITY_STUDENTS_TABLE_STATISTICS_QUERY,
                PostgresStatisticsQueries.CREATE_UNIVERSITY_STUDENT_APPEARANCE_TABLE_STATISTICS_QUERY
        );
    }

    protected final List< String > getPreparedStatements () {
        return List.of(
                PostgresPreparedQueryParams.PREPARE_GET_TEACHERS_BY_NAME_AND_EMAIL
        );
    }

    protected final synchronized <T> void analyze (
            final Collection<T> someList,
            final Consumer<T> someConsumer
    ) {
        someList.forEach( someConsumer );
    }

    protected final synchronized <T, V> void analyze (
            final Map< T, V > someList,
            final BiConsumer<T, V> someConsumer
    ) {
        someList.forEach( someConsumer );
    }

    protected final <T> boolean isCollectionNotEmpty ( final Collection<T> collection ) {
        return super.objectIsNotNull( collection ) && !collection.isEmpty();
    }

    protected final <T, V> boolean isCollectionNotEmpty ( final Map<T, V> collection ) {
        return super.objectIsNotNull( collection ) && !collection.isEmpty();
    }
}
