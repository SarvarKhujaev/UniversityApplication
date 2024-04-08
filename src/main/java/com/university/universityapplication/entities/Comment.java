package com.university.universityapplication.entities;

import com.university.universityapplication.constans.postgres_constants.postgres_constraints_constants.PostgresConstraintsValues;
import com.university.universityapplication.constans.postgres_constants.postgres_constraints_constants.PostgresConstraints;
import com.university.universityapplication.constans.postgres_constants.PostgreSqlFunctions;
import com.university.universityapplication.constans.postgres_constants.PostgreSqlSchema;
import com.university.universityapplication.constans.postgres_constants.PostgreSqlTables;
import com.university.universityapplication.constans.entities_constants.ErrorMessages;
import com.university.universityapplication.constans.hibernate.HibernateCacheRegions;
import com.university.universityapplication.inspectors.TimeInspector;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Table;
import jakarta.persistence.*;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.*;

import java.util.Date;

@Entity( name = PostgreSqlTables.COMMENTS )
@Table(
        name = PostgreSqlTables.COMMENTS,
        schema = PostgreSqlSchema.UNIVERSITY
)
@Cacheable
@Cache(
        usage = CacheConcurrencyStrategy.READ_ONLY,
        region = HibernateCacheRegions.COMMENT_REGION
)
@Check(
        name = PostgresConstraints.COMMENT_TABLE_CONSTRAINT,
        constraints = PostgresConstraintsValues.COMMENT_TABLE_CONSTRAINT_VALUE
)
/*
id  | comment |      created_date       | mark | student_id | lesson_id
*/
public final class Comment extends TimeInspector {
    public long getId() {
        return this.id;
    }

    public byte getMark() {
        return this.mark;
    }

    public Date getCreatedDate() {
        return this.createdDate;
    }

    public String getComment () {
        return this.comment;
    }

    public void setComment ( final String comment ) {
        this.comment = comment;
    }

    public Lesson getLesson() {
        return this.lesson;
    }

    public void setLesson ( final Lesson lesson ) {
        this.lesson = lesson;
    }

    public Student getStudent() {
        return this.student;
    }

    public void setStudent ( final Student student ) {
        this.student = student;
    }

    @Id
    @GeneratedValue(  strategy = GenerationType.IDENTITY )
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

    @Size(
            min = 5,
            max = 200,
            message = ErrorMessages.VALUE_OUT_OF_RANGE
    )
    @NotNull( message = ErrorMessages.NULL_VALUE )
    @NotBlank( message = ErrorMessages.NULL_VALUE )
    @NotEmpty( message = ErrorMessages.NULL_VALUE )
    @Column(
            length = 200,
            nullable = false,
            columnDefinition = "VARCHAR( 200 )"
    )
    private String comment;

    @NotNull( message = ErrorMessages.NULL_VALUE )
    @Column(
            columnDefinition = "SMALLINT DEFAULT 5"
    )
    /*
    оценка урока от студента
    по умолчанию будем ставить высшую оценку
    Интервал оценок 1 - 5
    */
    private final byte mark = 5;

    @Immutable
    @NotNull( message = ErrorMessages.NULL_VALUE )
    @ManyToOne(
            fetch = FetchType.EAGER,
            cascade = CascadeType.ALL,
            targetEntity = Student.class
    )
    private Student student;

    /*
    урок к которому оставлен комментарий
    */
    @NotNull( message = ErrorMessages.NULL_VALUE )
    @Immutable
    @PartitionKey
    @ManyToOne(
            fetch = FetchType.EAGER,
            cascade = CascadeType.ALL,
            targetEntity = Lesson.class
    )
    private Lesson lesson;

    public Comment () {}
}
