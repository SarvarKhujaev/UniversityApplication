package com.university.universityapplication.entities;

import com.university.universityapplication.entities.query_result_mapper_entities.StudentLessonAppearanceStat;
import com.university.universityapplication.constans.entities_constants.LessonAppearanceTypes;
import com.university.universityapplication.constans.postgres_constants.PostgreSqlFunctions;
import com.university.universityapplication.constans.hibernate.HibernateNativeNamedQueries;
import com.university.universityapplication.constans.postgres_constants.PostgreSqlSchema;
import com.university.universityapplication.constans.postgres_constants.PostgreSqlTables;
import com.university.universityapplication.constans.entities_constants.ErrorMessages;
import com.university.universityapplication.constans.hibernate.HibernateCacheRegions;
import com.university.universityapplication.inspectors.TimeInspector;

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
        usage = CacheConcurrencyStrategy.READ_ONLY,
        region = HibernateCacheRegions.STUDENT_APPEARANCE_IN_LESSON_REGION
)
@SqlResultSetMappings(
        value = {
                @SqlResultSetMapping(
                        name = HibernateNativeNamedQueries.GET_GROUPED_STUDENTS_STATS_FOR_LESSON_APPEARANCE_SETTER,
                        classes = {
                                @ConstructorResult(
                                        targetClass = StudentLessonAppearanceStat.class,
                                        columns = {
                                                @ColumnResult(
                                                        name = "lessonsCount", type = Long.class
                                                ),
                                                @ColumnResult(
                                                        name = "lessonAppearanceTypes", type = LessonAppearanceTypes.class
                                                )
                                        }
                                )
                        }
                )
        }
)
@org.hibernate.annotations.NamedNativeQueries(
        value = {
                @org.hibernate.annotations.NamedNativeQuery(
                        name = HibernateNativeNamedQueries.GET_GROUPED_STUDENTS_STATS_FOR_LESSON_APPEARANCE,
                        query = HibernateNativeNamedQueries.GET_GROUPED_STUDENTS_STATS_FOR_LESSON_APPEARANCE_QUERY,
                        timeout = 1,
                        readOnly = true,
                        cacheable = true,
                        resultClass = StudentLessonAppearanceStat.class,
                        resultSetMapping = HibernateNativeNamedQueries.GET_GROUPED_STUDENTS_STATS_FOR_LESSON_APPEARANCE_SETTER,
                        comment = """
                                вычисляем данные о посещении студентом
                                всех его занятий за все время
                                группируем по типу посещения
                                """
                )
        }
)
public final class StudentAppearanceInLessons extends TimeInspector {
    public long getId() {
        return this.id;
    }

    public void setId ( final long id ) {
        this.id = id;
    }

    public Date getAppearanceDate() {
        return this.appearanceDate;
    }

    public Student getStudent() {
        return this.student;
    }

    public void setStudent ( final Student student ) {
        this.student = student;
    }

    public Lesson getLesson() {
        return this.lesson;
    }

    public void setLesson ( final Lesson lesson ) {
        this.lesson = lesson;
    }

    public LessonAppearanceTypes getLessonAppearanceTypes() {
        return this.lessonAppearanceTypes;
    }

    public void setLessonAppearanceTypes ( final LessonAppearanceTypes lessonAppearanceTypes ) {
        this.lessonAppearanceTypes = lessonAppearanceTypes;
    }

    public void updateLessonAppearance () {
        this.setLessonAppearanceTypes(
                super.getLessonAppearanceTypesDueToAppearanceTime(
                        this.getAppearanceDate(),
                        this.getLesson().getLessonDate()
                )
        );
    }

    @Id
    @GeneratedValue(  strategy = GenerationType.IDENTITY )
    private long id;

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
    @NotNull( message = ErrorMessages.NULL_VALUE )
    @Enumerated( value = EnumType.STRING )
    @Column(
            name = "lesson_appearance_types",
            nullable = false,
            columnDefinition = "DEFAULT 'ABSENT'"
    )
    @PartitionKey
    private LessonAppearanceTypes lessonAppearanceTypes = LessonAppearanceTypes.ABSENT;

    public StudentAppearanceInLessons () {}
}
