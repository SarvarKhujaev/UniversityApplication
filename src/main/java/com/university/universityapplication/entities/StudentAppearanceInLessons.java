package com.university.universityapplication.entities;

import com.university.universityapplication.inspectors.TimeInspector;
import com.university.universityapplication.constans.*;

import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.PartitionKey;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Cache;

import jakarta.validation.constraints.NotNull;
import jakarta.persistence.*;
import java.util.Date;

/*
хранит данные о посещениях студента уроков
*/
@Entity( name = PostgreSqlTables.STUDENT_APPEARANCE_IN_LESSONS )
@Table(
        name = PostgreSqlTables.STUDENT_APPEARANCE_IN_LESSONS,
        schema = PostgreSqlSchema.UNIVERSITY
)
@Cacheable
@Cache(
        usage = CacheConcurrencyStrategy.READ_ONLY
)
public final class StudentAppearanceInLessons extends TimeInspector {
    @Id
    @GeneratedValue(  strategy = GenerationType.IDENTITY )
    private Long id;

    @Immutable
    @PartitionKey
    @NotNull( message = ErrorMessages.NULL_VALUE )
    @Column(
            name = "appearance_date",
            nullable = false,
            updatable = false,
            columnDefinition = PostgreSqlFunctions.NOW
    )
    private final Date appearanceDate = super.newDate(); // дата посещения занятия

    @NotNull( message = ErrorMessages.NULL_VALUE )
    @OneToOne(
            targetEntity = Student.class,
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER
    )
    @Immutable
    private Student student;

    @NotNull( message = ErrorMessages.NULL_VALUE )
    @OneToOne(
            targetEntity = Lesson.class,
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER
    )
    @Immutable
    private Lesson lesson;

    // https://www.baeldung.com/jpa-default-column-values
    @Immutable
    @NotNull( message = ErrorMessages.NULL_VALUE )
    @Enumerated( value = EnumType.STRING )
    @Column(
            name = "lesson_appearance_types",
            nullable = false,
            updatable = false
    )
    @PartitionKey
    private LessonAppearanceTypes lessonAppearanceTypes = LessonAppearanceTypes.IN_TIME;
}
