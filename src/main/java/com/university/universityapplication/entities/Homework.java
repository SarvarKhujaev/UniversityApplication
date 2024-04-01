package com.university.universityapplication.entities;

import com.university.universityapplication.constans.postgres_constants.postgres_constraints_constants.PostgresConstraintsValues;
import com.university.universityapplication.constans.postgres_constants.postgres_constraints_constants.PostgresConstraints;
import com.university.universityapplication.constans.postgres_constants.PostgreSqlFunctions;
import com.university.universityapplication.constans.postgres_constants.PostgreSqlSchema;
import com.university.universityapplication.constans.postgres_constants.PostgreSqlTables;
import com.university.universityapplication.constans.entities_constants.ErrorMessages;
import com.university.universityapplication.inspectors.TimeInspector;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.persistence.*;

import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.PartitionKey;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Check;

import java.util.Date;

/*
после каждого занятия всем студентам дается ДЗ

 id | created_date | homework_description | lesson_id
----+--------------+----------------------+-----------
*/
@Entity( name = PostgreSqlTables.HOMEWORK )
@Table(
        name = PostgreSqlTables.HOMEWORK,
        schema = PostgreSqlSchema.UNIVERSITY
)
@Cacheable
@org.hibernate.annotations.Cache(
        usage = CacheConcurrencyStrategy.READ_ONLY
)
@Check(
        name = PostgresConstraints.HOMEWORK_TABLE_CONSTRAINT,
        constraints = PostgresConstraintsValues.HOMEWORK_TABLE_CONSTRAINT_VALUE
)
public final class Homework extends TimeInspector {
    public long getId() {
        return this.id;
    }

    public void setId( final long id ) {
        this.id = id;
    }

    public Date getCreatedDate( final Date createdDate ) {
        return this.createdDate;
    }

    public String getHomeworkDescription() {
        return this.homeworkDescription;
    }

    public void setHomeworkDescription( final String homeworkDescription ) {
        this.homeworkDescription = homeworkDescription;
    }

    public Lesson getLesson() {
        return this.lesson;
    }

    public void setLesson( final Lesson lesson ) {
        this.lesson = lesson;
    }

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private long id;

    @Immutable
    @PartitionKey
    @NotNull( message = ErrorMessages.NULL_VALUE )
    @Column(
            name = "created_date",
            nullable = false,
            updatable = false,
            columnDefinition = PostgreSqlFunctions.NOW
    )
    private final Date createdDate = super.newDate(); // дата создания

    /*
    описание самого ДЗ
    */
    @Size(
            min = 50,
            max = 500,
            message = ErrorMessages.VALUE_OUT_OF_RANGE
    )
    @NotNull( message = ErrorMessages.NULL_VALUE )
    @NotBlank( message = ErrorMessages.NULL_VALUE )
    @Column(
            columnDefinition = "VARCHAR( 500 )",
            nullable = false,
            name = "homework_description"
    )
    private String homeworkDescription;

    /*
    ДЗ за конкретный урок
    */
    @NotNull( message = ErrorMessages.NULL_VALUE )
    @Immutable
    @PartitionKey
    @OneToOne(
            targetEntity = Lesson.class,
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY
    )
    private Lesson lesson;
}
