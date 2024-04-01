package com.university.universityapplication.entities;

import com.university.universityapplication.constans.postgres_constants.postgres_constraints_constants.PostgresConstraintsValues;
import com.university.universityapplication.constans.postgres_constants.postgres_constraints_constants.PostgresConstraints;
import com.university.universityapplication.constans.postgres_constants.PostgreSqlFunctions;
import com.university.universityapplication.constans.postgres_constants.PostgreSqlSchema;
import com.university.universityapplication.constans.postgres_constants.PostgreSqlTables;
import com.university.universityapplication.constans.entities_constants.ErrorMessages;
import com.university.universityapplication.inspectors.TimeInspector;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.persistence.*;

import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.PartitionKey;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Check;

import java.util.Date;

/*
хранит данные об оценках студента
поставленные за конкретную ДЗ
от преподавателя

 id | created_date | mark_for_homework | teacher_comment | student_id | teacher_id
----+--------------+-------------------+-----------------+------------+------------
*/
@Entity( name = PostgreSqlTables.STUDENT_MARKS )
@Table(
        name = PostgreSqlTables.STUDENT_MARKS,
        schema = PostgreSqlSchema.UNIVERSITY
)
@Cacheable
@org.hibernate.annotations.Cache(
        usage = CacheConcurrencyStrategy.READ_ONLY
)
@Check(
        name = PostgresConstraints.STUDENT_MARKS_TABLE_CONSTRAINT,
        constraints = PostgresConstraintsValues.STUDENT_MARKS_TABLE_CONSTRAINT_VALUE
)
public final class StudentMarks extends TimeInspector {
    public long getId() {
        return this.id;
    }

    public void setId( final long id ) {
        this.id = id;
    }

    public Date getCreatedDate() {
        return this.createdDate;
    }

    public byte getMarkForHomework() {
        return this.markForHomework;
    }

    public void setMarkForHomework( final byte markForHomework ) {
        this.markForHomework = markForHomework;
    }

    public String getTeacherComment() {
        return this.teacherComment;
    }

    public void setTeacherComment( final String teacherComment ) {
        this.teacherComment = teacherComment;
    }

    public Student getStudent() {
        return this.student;
    }

    public void setStudent( final Student student ) {
        this.student = student;
    }

    public Teacher getTeacher() {
        return this.teacher;
    }

    public void setTeacher( final Teacher teacher ) {
        this.teacher = teacher;
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
    оценка от перподавателя
    */
    @NotNull( message = ErrorMessages.NULL_VALUE )
    @Positive( message = ErrorMessages.VALUE_OUT_OF_RANGE )
    @Column(
            columnDefinition = "SMALLINT DEFAULT 5",
            updatable = false,
            nullable = false,
            name = "mark_for_homework"
    )
    @Immutable
    private byte markForHomework = 5;

    /*
    комментарий от перподавателя
    */
    @Size(
            min = 20,
            max = 200,
            message = ErrorMessages.VALUE_OUT_OF_RANGE
    )
    @NotNull( message = ErrorMessages.NULL_VALUE )
    @Column(
            columnDefinition = "VARCHAR( 200 )",
            updatable = false,
            nullable = false,
            name = "teacher_comment"
    )
    @Immutable
    private String teacherComment;

    @NotNull( message = ErrorMessages.NULL_VALUE )
    @OneToOne(
            targetEntity = Student.class,
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER
    )
    @Immutable
    @PartitionKey
    /*
    студент которому поставили оценку
    */
    private Student student;

    @NotNull( message = ErrorMessages.NULL_VALUE )
    @OneToOne(
            targetEntity = Teacher.class,
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER
    )
    @Immutable
    @PartitionKey
    /*
    учитель который поставил оценку
    */
    private Teacher teacher;
}
