package com.university.universityapplication.entities;

import com.university.universityapplication.constans.PostgreSqlFunctions;
import com.university.universityapplication.constans.PostgreSqlSchema;
import com.university.universityapplication.constans.PostgreSqlTables;
import com.university.universityapplication.inspectors.TimeInspector;
import com.university.universityapplication.constans.ErrorMessages;

import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.PartitionKey;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Cache;

import jakarta.validation.constraints.*;
import jakarta.persistence.*;

import java.util.Date;
import java.util.List;

@Entity( name = PostgreSqlTables.STUDENTS )
@Table(
        name = PostgreSqlTables.STUDENTS,
        schema = PostgreSqlSchema.UNIVERSITY
)
@Cacheable
@Cache(
        usage = CacheConcurrencyStrategy.READ_WRITE
)
public class Student extends TimeInspector {
    public Long getId() {
        return this.id;
    }

    public void setId ( final Long id ) {
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

    public String getStudentShortDescription() {
        return this.studentShortDescription;
    }

    public void setStudentShortDescription (
            final String studentShortDescription
    ) {
        this.studentShortDescription = studentShortDescription;
    }

    public List< Group > getGroupList() {
        return this.groupList;
    }

    public void setGroupList (
            final List< Group > groupList
    ) {
        this.groupList = groupList;
    }

    public List< EducationDirection > getEducationDirectionList() {
        return this.educationDirectionList;
    }

    public void setEducationDirectionList (
            final List< EducationDirection > educationDirectionList
    ) {
        this.educationDirectionList = educationDirectionList;
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
    private final Date createdDate = super.newDate(); // дата создания аккаунта

    @Size(
            min = 18,
            max = 100,
            message = ErrorMessages.VALUE_OUT_OF_RANGE
    )
    @Positive
    @NotNull( message = ErrorMessages.NULL_VALUE )
    @NotBlank( message = ErrorMessages.NULL_VALUE )
    @Column(
            nullable = false,
            columnDefinition = "TINYINT NOT NULL DEFAULT 18"
    )
    private byte age;

    @Size(
            min = 5,
            max = 50,
            message = ErrorMessages.VALUE_OUT_OF_RANGE
    )
    @NotNull( message = ErrorMessages.NULL_VALUE )
    @NotBlank( message = ErrorMessages.NULL_VALUE )
    @Column(
            columnDefinition = "VARCHAR( 50 ) NOT NULL",
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
            min = 5,
            max = 50,
            message = ErrorMessages.VALUE_OUT_OF_RANGE
    )
    @NotNull( message = ErrorMessages.NULL_VALUE )
    @NotBlank( message = ErrorMessages.NULL_VALUE )
    @Column(
            columnDefinition = "VARCHAR( 50 ) NOT NULL",
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
            columnDefinition = "VARCHAR( 50 ) NOT NULL",
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
            columnDefinition = "VARCHAR( 50 ) NOT NULL",
            nullable = false,
            length = 50,
            name = "father_name"
    )
    private String fatherName;

    /*
    короткое описание самого студента,
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
            columnDefinition = "VARCHAR( 200 ) NOT NULL",
            nullable = false,
            length = 200,
            name = "student_short_description"
    )
    private String studentShortDescription;

    @NotNull( message = ErrorMessages.NULL_VALUE )
    @OneToMany(
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            targetEntity = Group.class,
            orphanRemoval = true
    )
    @Column(
            name = "group_list"
    )
    @OrderBy( value = "groupName DESC, createdDate DESC" )
    @JoinColumn(
            name = "student_id",
            table = PostgreSqlTables.GROUPS
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
    /*
    список групп к которым прикреплен студент
    */
    private List< Group > groupList = super.newList();

    @NotNull( message = ErrorMessages.NULL_VALUE )
    @OneToMany(
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            targetEntity = EducationDirection.class,
            orphanRemoval = true
    )
    @Column(
            name = "education_direction_list"
    )
    @OrderBy( value = "directionName DESC" )
    @JoinColumn(
            name = "student_id",
            table = PostgreSqlTables.EDUCATION_DIRECTIONS
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
    /*
    список направлений обучения, которые нужны студенту
    */
    private List< EducationDirection > educationDirectionList = super.newList();
}
