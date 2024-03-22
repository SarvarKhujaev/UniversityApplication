package com.university.universityapplication.entities;

import com.university.universityapplication.inspectors.TimeInspector;
import com.university.universityapplication.constans.*;

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
        usage = CacheConcurrencyStrategy.READ_ONLY
)
@Check(
        name = PostgresConstraints.COMMENT_TABLE_CONSTRAINT,
        constraints = """
                mark >= 1 AND mark <= 5
                """
)
public final class Comment extends TimeInspector {
    public Long getId() {
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
    private Long id;

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
            columnDefinition = "VARCHAR( 200 ) NOT NULL"
    )
    private String comment;

    @Size(
            min = 1,
            max = 5,
            message = ErrorMessages.VALUE_OUT_OF_RANGE
    )
    @NotNull( message = ErrorMessages.NULL_VALUE )
    @Column(
            columnDefinition = "SMALLINT DEFAULT 5"
    )
    /*
    оценка урока
    */
    private final byte mark = 5;

    @NotNull( message = ErrorMessages.NULL_VALUE )
    @ManyToOne(
            fetch = FetchType.EAGER,
            cascade = CascadeType.ALL,
            targetEntity = Student.class
    )
    @Immutable
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
    @JoinColumn(
            name = "comment_id",
            table = PostgreSqlTables.COMMENTS
    )
    private Lesson lesson;

    public Comment () {}
}
