package com.university.universityapplication.entities;

import com.university.universityapplication.constans.postgres_constants.postgres_constraints_constants.PostgresConstraintsValues;
import com.university.universityapplication.constans.postgres_constants.postgres_constraints_constants.PostgresConstraints;
import com.university.universityapplication.entities.query_result_mapper_entities.TeacherAverageMark;
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
import org.hibernate.annotations.Check;

import jakarta.validation.constraints.*;
import jakarta.persistence.*;

import java.util.Date;
import java.util.List;

@Entity( name = PostgreSqlTables.TEACHERS )
@Table(
        name = PostgreSqlTables.TEACHERS,
        schema = PostgreSqlSchema.UNIVERSITY
)
@Cacheable
@org.hibernate.annotations.Cache(
        usage = CacheConcurrencyStrategy.READ_WRITE,
        region = HibernateCacheRegions.TEACHER_REGION
)
@Check(
        name = PostgresConstraints.TEACHER_TABLE_CONSTRAINT,
        constraints = PostgresConstraintsValues.TEACHER_TABLE_CONSTRAINT_VALUE
)
@Check(
        name = PostgresConstraints.TEACHER_TABLE_PHONE_NUMBER_CONSTRAINT,
        constraints = PostgresConstraintsValues.PHONE_NUMBER_CONSTRAINT_VALUE
)
@SqlResultSetMappings(
        @SqlResultSetMapping(
                name = HibernateNativeNamedQueries.GET_TEACHER_AVERAGE_MARKS_SETTER,
                classes = {
                        @ConstructorResult(
                                targetClass = TeacherAverageMark.class,
                                columns = {
                                        @ColumnResult(
                                                name = "averageMark", type = Float.class
                                        ),
                                        @ColumnResult(
                                                name = "lessonCount", type = Long.class
                                        ),
                                        @ColumnResult(
                                                name = "averageMarkNaming", type = String.class
                                        )
                                }
                        )
                }
        )
)
@org.hibernate.annotations.NamedNativeQueries(
        value = {
                @org.hibernate.annotations.NamedNativeQuery(
                        name = HibernateNativeNamedQueries.GET_TEACHER_AVERAGE_MARKS,
                        query = HibernateNativeNamedQueries.GET_TEACHER_AVERAGE_MARKS_QUERY,
                        timeout = 1,
                        readOnly = true,
                        cacheable = true,
                        flushMode = org.hibernate.annotations.FlushModeType.COMMIT,
                        resultClass = TeacherAverageMark.class,
                        resultSetMapping = HibernateNativeNamedQueries.GET_TEACHER_AVERAGE_MARKS_SETTER
                )
        }
)
/*
 id | age | birth_date |      created_date       |      email      | father_name | name  | surname | teacher_short_description | phone_number
----+-----+------------+-------------------------+-----------------+-------------+-------+---------+---------------------------+---------------
*/
public final class Teacher extends TimeInspector {
    public long getId() {
        return this.id;
    }

    public void setId ( final long id ) {
        this.id = id;
    }

    public Date getCreatedDate () {
        return this.createdDate;
    }

    public byte getAge() {
        return this.age;
    }

    public void setAge ( final byte age ) {
        this.age = age;
    }

    public String getName() {
        return this.name;
    }

    public void setName ( final String name ) {
        this.name = name;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail ( final String email ) {
        this.email = email;
    }

    public String getSurname() {
        return this.surname;
    }

    public void setSurname ( final String surname ) {
        this.surname = surname;
    }

    public String getBirthDate() {
        return this.birthDate;
    }

    public void setBirthDate ( final String birthDate ) {
        this.birthDate = birthDate;
    }

    public String getFatherName() {
        return this.fatherName;
    }

    public void setFatherName ( final String fatherName ) {
        this.fatherName = fatherName;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public void setPhoneNumber ( final String phoneNumber ) {
        this.phoneNumber = phoneNumber;
    }

    public String getStudentShortDescription() {
        return this.teacherShortDescription;
    }

    public void setStudentShortDescription (
            final String studentShortDescription
    ) {
        this.teacherShortDescription = studentShortDescription;
    }

    public String getTeacherShortDescription() {
        return this.teacherShortDescription;
    }

    public void setTeacherShortDescription ( final String teacherShortDescription ) {
        this.teacherShortDescription = teacherShortDescription;
    }

    public List< Group > getGroupList() {
        return this.groupList;
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

    @NotNull( message = ErrorMessages.NULL_VALUE )
    @Column(
            nullable = false,
            columnDefinition = "SMALLINT DEFAULT 18"
    )
    private byte age = 18;

    @Size(
            min = 5,
            max = 50,
            message = ErrorMessages.VALUE_OUT_OF_RANGE
    )
    @NotNull( message = ErrorMessages.NULL_VALUE )
    @NotBlank( message = ErrorMessages.NULL_VALUE )
    @Column(
            columnDefinition = "VARCHAR( 50 )",
            nullable = false,
            length = 50
    )
    private String name;

    @Size(
            min = 5,
            max = 50,
            message = ErrorMessages.VALUE_OUT_OF_RANGE
    )
    @Email( message = ErrorMessages.WRONG_EMAIL )
    @NotNull( message = ErrorMessages.NULL_VALUE )
    @NotBlank( message = ErrorMessages.NULL_VALUE )
    @Column(
            nullable = false,
            unique = true,
            length = 50
    )
    @PartitionKey
    private String email;

    @Size(
            min = 13,
            max = 13,
            message = ErrorMessages.VALUE_OUT_OF_RANGE
    )
    @NotNull( message = ErrorMessages.NULL_VALUE )
    @NotBlank( message = ErrorMessages.NULL_VALUE )
    @Column(
            columnDefinition = "VARCHAR( 13 ) DEFAULT '+9989771221' || random_between( 10, 100 )",
            nullable = false,
            unique = true,
            length = 13,
            name = "phone_number"
    )
    @PartitionKey
    private String phoneNumber;

    @Size(
            min = 5,
            max = 50,
            message = ErrorMessages.VALUE_OUT_OF_RANGE
    )
    @NotNull( message = ErrorMessages.NULL_VALUE )
    @NotBlank( message = ErrorMessages.NULL_VALUE )
    @Column(
            columnDefinition = "VARCHAR( 50 )",
            nullable = false,
            length = 50
    )
    private String surname;

    @Size(
            min = 5,
            max = 50,
            message = ErrorMessages.VALUE_OUT_OF_RANGE
    )
    @NotNull( message = ErrorMessages.NULL_VALUE )
    @NotBlank( message = ErrorMessages.NULL_VALUE )
    @Column(
            columnDefinition = "VARCHAR( 50 )",
            nullable = false,
            length = 50,
            name = "birth_date"
    )
    private String birthDate;

    @Size(
            min = 5,
            max = 50,
            message = ErrorMessages.VALUE_OUT_OF_RANGE
    )
    @NotNull( message = ErrorMessages.NULL_VALUE )
    @NotBlank( message = ErrorMessages.NULL_VALUE )
    @Column(
            columnDefinition = "VARCHAR( 50 )",
            nullable = false,
            length = 50,
            name = "father_name"
    )
    private String fatherName;

    /*
    короткое описание самого преподавателя,
    которое он заполняет при заполнении анкеты
    описывает свои предпочтения и планы
    */
    @Size(
            min = 5,
            max = 200,
            message = ErrorMessages.VALUE_OUT_OF_RANGE
    )
    @NotNull( message = ErrorMessages.NULL_VALUE )
    @NotBlank( message = ErrorMessages.NULL_VALUE )
    @Column(
            columnDefinition = "VARCHAR( 200 )",
            nullable = false,
            length = 200,
            name = "teacher_short_description"
    )
    private String teacherShortDescription;

    @NotNull( message = ErrorMessages.NULL_VALUE )
    @OneToMany(
            fetch = FetchType.LAZY,
            cascade = CascadeType.REFRESH,
            targetEntity = Group.class,
            orphanRemoval = true
    )
    @Column(
            name = "group_list"
    )
    @OrderBy( value = "groupName DESC, createdDate DESC" )
    @JoinColumn( name = "teacher_id" )
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
    private final List< Group > groupList = super.newList();

    public Teacher () {}
}
