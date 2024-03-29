package com.university.universityapplication.inspectors;

import com.university.universityapplication.constans.postgres_constants.postgres_index_constants.PostgresIndexesCreateQueries;
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
                PostgreSqlTables.EDUCATION_DIRECTIONS
        );
    }

    protected final List< Class<?> > getClassesReferences () {
        return List.of(
                Group.class,
                Lesson.class,
                Teacher.class,
                Student.class,
                Comment.class,
                EducationDirection.class,
                StudentAppearanceInLessons.class
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
