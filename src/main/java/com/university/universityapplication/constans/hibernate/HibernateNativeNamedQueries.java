package com.university.universityapplication.constans.hibernate;

public final class HibernateNativeNamedQueries {
    public static final String GET_ALL_GROUPS_FOR_CURRENT_USER_BY_USER_ID = "GET_ALL_GROUPS_FOR_CURRENT_USER_BY_USER_ID";

    public static final String GET_ALL_GROUPS_FOR_CURRENT_USER_BY_USER_ID_QUERY = """
            SELECT *
            FROM university.groups
            WHERE id IN (
                    SELECT s.group_id
                    FROM university.STUDENTS_WITH_GROUPS_JOIN_TABLE s
                    WHERE s.student_id = :student_id
                )
            """;

    public static final String GET_TEACHER_AVERAGE_MARKS = "GET_TEACHER_AVERAGE_MARKS";

    public static final String GET_TEACHER_AVERAGE_MARKS_SETTER = "GET_TEACHER_AVERAGE_MARKS_SETTER";

    public static final String GET_TEACHER_AVERAGE_MARKS_QUERY = """
            SELECT COALESCE( average_mark, 0 ) AS averageMark, lessonCount,
                CASE
                WHEN average_mark BETWEEN 0 AND 2 THEN 'horrible'
                WHEN average_mark BETWEEN 2 AND 3 THEN 'normal'
                WHEN average_mark BETWEEN 3 AND 4 THEN 'medium'
                ELSE 'perfect'
                END averageMarkNaming
            FROM (
                SELECT avg( c.mark ) AS average_mark, -- средняя оценка занятия
                COUNT( l.id ) AS lessonCount -- количество занятий которые провел препод

                FROM university.teachers t -- находим самого учителя

                INNER JOIN university.groups g ON g.teacher_id = :teacher_id -- находим все группы за которые он отвечает

                INNER JOIN university.lessons l ON l.group_id = g.id -- находим все занятия в этих группах

                INNER JOIN university.comments c ON c.lesson_id = l.id -- находим вс комметарии к этим занятиям

                WHERE g.teacher_id = :teacher_id
            );
            """;

    public static final String GET_GROUPED_STUDENTS_STATS_FOR_LESSON_APPEARANCE = "GET_GROUPED_STUDENTS_STATS_FOR_LESSON_APPEARANCE";

    public static final String GET_GROUPED_STUDENTS_STATS_FOR_LESSON_APPEARANCE_SETTER = "GET_GROUPED_STUDENTS_STATS_FOR_LESSON_APPEARANCE_SETTER";

    public static final String GET_GROUPED_STUDENTS_STATS_FOR_LESSON_APPEARANCE_QUERY = """
            SELECT s.lesson_appearance_types AS lessonAppearanceTypes,
            COUNT( s.lesson_appearance_types ) AS lessonsCount, -- общее количество занятий студента
            FROM university.STUDENT_APPEARANCE_IN_LESSONS s
            WHERE s.student_id = :student_id
            GROUP BY s.lesson_appearance_types;
            """;
}
