package com.university.universityapplication.inspectors;

import com.university.universityapplication.constans.postgres_constants.PostgreSqlSchema;
import com.university.universityapplication.constans.postgres_constants.PostgresCommonCommands;
import com.university.universityapplication.constans.postgres_constants.PostgresCreateValues;
import com.university.universityapplication.constans.postgres_constants.postgres_materialized_view_constants.PostgresMaterializedViewMethods;
import com.university.universityapplication.constans.postgres_constants.postgres_prepared_constants.PostgresPreparedQueryParams;
import com.university.universityapplication.constans.postgres_constants.postgres_statistics_constants.PostgresStatisticsQueries;
import com.university.universityapplication.constans.postgres_constants.postgres_index_constants.PostgresIndexesCreateQueries;
import com.university.universityapplication.constans.postgres_constants.postgres_extensions.PostgresExtensions;
import com.university.universityapplication.constans.postgres_constants.PostgreSqlTables;
import com.university.universityapplication.constans.hibernate.HibernateCacheRegions;
import com.university.universityapplication.entities.*;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.*;

public class CollectionsInspector extends DataValidateInspector {
    protected CollectionsInspector () {}

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

    protected final List< String > getRangePartitionsTablesList() {
        return List.of(
                PostgreSqlTables.LESSONS,
                PostgreSqlTables.COMMENTS,
                PostgreSqlTables.HOMEWORK,
                PostgreSqlTables.STUDENT_MARKS
        );
    }

    protected final List< String > getListPartitionsTablesList() {
        return List.of(
                PostgreSqlTables.EDUCATION_DIRECTIONS,
                PostgreSqlTables.STUDENT_MARKS,
                PostgreSqlTables.GROUPS
        );
    }

    protected final List< String > getHashPartitionsTablesList() {
        return List.of(
                PostgreSqlTables.STUDENTS,
                PostgreSqlTables.TEACHERS,
                PostgreSqlTables.GROUPS
        );
    }

    protected final List< String > getPostgresExtensions () {
        return List.of(
                PostgresExtensions.CREATE_EXTENSION_PG_PREWARM,
                PostgresExtensions.CREATE_EXTENSION_PG_STAT_TUPLE,
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

                PostgresIndexesCreateQueries.UNIVERSITY_STUDENT_MARKS_INDEX,
                PostgresIndexesCreateQueries.UNIVERSITY_STUDENT_MARKS_STUDENT_AND_TEACHER_REF_INDEX,

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

    protected final List< String > getAllCacheRegionsNames () {
        return List.of(
                HibernateCacheRegions.GROUP_REGION,
                HibernateCacheRegions.LESSON_REGION,
                HibernateCacheRegions.STUDENT_REGION,
                HibernateCacheRegions.TEACHER_REGION,
                HibernateCacheRegions.COMMENT_REGION,
                HibernateCacheRegions.HOMEWORK_REGION,
                HibernateCacheRegions.STUDENT_MARKS_REGION,
                HibernateCacheRegions.STUDENT_APPEARANCE_IN_LESSON_REGION
        );
    }

    protected final List< String > getAllMaterializedViewsNames () {
        final List< String > materializedViews = this.newList();

        this.analyze(
                this.getTablesList(),
                tableName -> materializedViews.add(
                        PostgresMaterializedViewMethods.CREATE_MATERIALIZED_VIEW.formatted(
                                String.join(
                                        "_",
                                        tableName,
                                        PostgresCreateValues.MATERIALIZED_VIEW.name()
                                ),
                                PostgresCommonCommands.SELECT_ALL_FROM.formatted(
                                        PostgreSqlSchema.UNIVERSITY,
                                        tableName
                                )
                        )
                )
        );

        return materializedViews;
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
}
