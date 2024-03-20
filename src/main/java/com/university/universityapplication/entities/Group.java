package com.university.universityapplication.entities;

import com.university.universityapplication.inspectors.TimeInspector;
import com.university.universityapplication.constans.*;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import jakarta.persistence.CascadeType;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import jakarta.persistence.*;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.*;

import java.util.Date;
import java.util.List;

@Entity( name = PostgreSqlTables.GROUPS )
@Table(
        name = PostgreSqlTables.GROUPS,
        schema = PostgreSqlSchema.UNIVERSITY
)
@Cacheable
@Cache(
        usage = CacheConcurrencyStrategy.READ_ONLY
)
@Check(
        name = PostgresConstraints.GROUP_TABLE_CONSTRAINT,
        constraints = """
                max_students_number > 3 AND students_number > 3
                """
)
public class Group extends TimeInspector {
    public Long getId() {
        return this.id;
    }

    public Teacher getTeacher() {
        return this.teacher;
    }

    public Date getCreatedDate() {
        return this.createdDate;
    }

    public String getGroupName() {
        return this.groupName;
    }

    public byte getStudentsNumber() {
        return this.studentsNumber;
    }

    public byte getMaxStudentsNumber() {
        return this.maxStudentsNumber;
    }

    public List<Student> getStudentList() {
        return this.studentList;
    }

    public EducationDirection getEducationDirection() {
        return this.educationDirection;
    }

    public void setId ( final Long id ) {
        this.id = id;
    }

    public void setTeacher ( final Teacher teacher ) {
        this.teacher = teacher;
    }

    public void setGroupName ( final String groupName ) {
        this.groupName = groupName;
    }

    public void setStudentsNumber ( final byte studentsNumber ) {
        this.studentsNumber = studentsNumber;
    }

    public void setMaxStudentsNumber ( final byte maxStudentsNumber ) {
        this.maxStudentsNumber = maxStudentsNumber;
    }

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
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
            max = 50,
            message = ErrorMessages.VALUE_OUT_OF_RANGE
    )
    @NotNull( message = ErrorMessages.NULL_VALUE )
    @NotBlank( message = ErrorMessages.NULL_VALUE )
    @Column(
            columnDefinition = "VARCHAR( 50 ) NOT NULL",
            updatable = false,
            nullable = false,
            unique = true,
            length = 50,
            name = "group_name"
    )
    @Immutable
    @PartitionKey
    private String groupName;

    /*
    максималное количество студентов в одной группе
    */
    @Size(
            min = 3,
            max = 20,
            message = ErrorMessages.VALUE_OUT_OF_RANGE
    )
    @NotNull( message = ErrorMessages.NULL_VALUE )
    @Column(
            columnDefinition = "TINYINT NOT NULL",
            nullable = false,
            name = "max_students_number"
    )
    @Positive( message = ErrorMessages.VALUE_MUST_BE_POSITIVE )
    private byte maxStudentsNumber;

    /*
    текущее количество студентов
    не может быть боьше параметра maxStudentsNumber
    */
    @Size(
            min = 3,
            max = 20,
            message = ErrorMessages.VALUE_OUT_OF_RANGE
    )
    @NotNull( message = ErrorMessages.NULL_VALUE )
    @Column(
            columnDefinition = "TINYINT NOT NULL",
            nullable = false,
            name = "students_number"
    )
    @Positive( message = ErrorMessages.VALUE_MUST_BE_POSITIVE )
    private byte studentsNumber;

    /*
    преподаватель группы
    */
    @PartitionKey
    @OneToOne(
            targetEntity = Teacher.class,
            cascade = CascadeType.PERSIST,
            fetch = FetchType.LAZY
    )
    private Teacher teacher;

    /*
название направления по которым проводятся занятия
*/
    @NotNull( message = ErrorMessages.NULL_VALUE )
    @Immutable
    @PartitionKey
    @Column(
            name = "education_direction",
            nullable = false,
            updatable = false
    )
    @OneToOne(
            orphanRemoval = true,
            targetEntity = EducationDirection.class,
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER
    )
    private EducationDirection educationDirection;

    /*
    список студентов которые прикреплены к этой группы
    */
    @NotNull( message = ErrorMessages.NULL_VALUE )
    @OrderBy(
            value = "name DESC, createdDate DESC"
    )
    @Column(
            name = "student_list"
    )
    @OneToMany(
            fetch = FetchType.LAZY,
            cascade = CascadeType.REFRESH,
            targetEntity = Student.class,
            orphanRemoval = true
    )
    /*
    Hibernate can also cache collections, and the @Cache annotation must be on added to the collection property.

    If the collection is made of value types (basic or embeddables mapped with @ElementCollection),
    the collection is stored as such.
    If the collection contains other entities (@OneToMany or @ManyToMany),
    the collection cache entry will store the entity identifiers only.
    */
    @org.hibernate.annotations.Cache(
            usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE
    )
    @Immutable
    private final List< Student > studentList = super.newList();

    @NotNull( message = ErrorMessages.NULL_VALUE )
    @OrderBy(
            value = "lessonDate DESC, lessonName ASC"
    )
    @Column(
            name = "lesson_list"
    )
    @OneToMany(
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            targetEntity = Lesson.class,
            orphanRemoval = true
    )
    /*
    Hibernate can also cache collections, and the @Cache annotation must be on added to the collection property.

    If the collection is made of value types (basic or embeddables mapped with @ElementCollection),
    the collection is stored as such.
    If the collection contains other entities (@OneToMany or @ManyToMany),
    the collection cache entry will store the entity identifiers only.
    */
    @org.hibernate.annotations.Cache(
            usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE
    )
    @Immutable
    private final List< Lesson > lessonList = super.newList();
}
