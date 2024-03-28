package com.university.universityapplication.inspectors;

import com.university.universityapplication.constans.postgres_constants.PostgreSqlTables;
import com.university.universityapplication.entities.*;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.*;

public class CollectionsInspector extends DataValidateInspector {
    protected CollectionsInspector () {}

    protected <T> List<T> emptyList () {
        return Collections.emptyList();
    }

    protected  <T> ArrayList<T> newList () {
        return new ArrayList<>();
    }

    protected <T, V> Map<T, V> newMap () {
        return new HashMap<>();
    }

    protected List< String > getTablesList () {
        return List.of(
                PostgreSqlTables.GROUPS,
                PostgreSqlTables.LESSONS,
                PostgreSqlTables.TEACHERS,
                PostgreSqlTables.COMMENTS,
                PostgreSqlTables.STUDENTS,
                PostgreSqlTables.EDUCATION_DIRECTIONS
        );
    }

    protected List< Class<?> > getClassesReferences () {
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

    protected synchronized <T> void analyze (
            final Collection<T> someList,
            final Consumer<T> someConsumer
    ) {
        someList.forEach( someConsumer );
    }

    protected synchronized <T, V> void analyze (
            final Map< T, V > someList,
            final BiConsumer<T, V> someConsumer
    ) {
        someList.forEach( someConsumer );
    }

    protected <T> boolean isCollectionNotEmpty ( final Collection<T> collection ) {
        return super.objectIsNotNull( collection ) && !collection.isEmpty();
    }

    protected <T, V> boolean isCollectionNotEmpty ( final Map<T, V> collection ) {
        return super.objectIsNotNull( collection ) && !collection.isEmpty();
    }
}
