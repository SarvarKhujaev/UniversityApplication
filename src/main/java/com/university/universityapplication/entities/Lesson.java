package com.university.universityapplication.entities;

import com.university.universityapplication.constans.postgres_constants.PostgreSqlFunctions;
import com.university.universityapplication.constans.postgres_constants.PostgreSqlSchema;
import com.university.universityapplication.constans.postgres_constants.PostgreSqlTables;
import com.university.universityapplication.constans.entities_constants.ErrorMessages;
import com.university.universityapplication.constans.entities_constants.LessonStatus;
import com.university.universityapplication.inspectors.TimeInspector;

import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.PartitionKey;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Cache;

import jakarta.validation.constraints.*;
import jakarta.persistence.*;

import java.util.Date;
import java.util.List;

@Entity( name = PostgreSqlTables.LESSONS )
@Table(
        name = PostgreSqlTables.LESSONS,
        schema = PostgreSqlSchema.UNIVERSITY
)
@Cacheable
@Cache(
        usage = CacheConcurrencyStrategy.READ_ONLY
)
public final class Lesson extends TimeInspector {
    public Long getId() {
        return this.id;
    }

    public void setId ( final Long id ) {
        this.id = id;
    }

    public Date getLessonDate() {
        return this.lessonDate;
    }

    public LessonStatus getLessonStatus() {
        return this.lessonStatus;
    }

    public void setLessonStatus( final LessonStatus lessonStatus ) {
        this.lessonStatus = lessonStatus;
    }

    public String getLessonName() {
        return this.lessonName;
    }

    public void setLessonName ( final String lessonName ) {
        this.lessonName = lessonName;
    }

    public List< Comment > getCommentList() {
        return this.commentList;
    }

    @Id
    @GeneratedValue(  strategy = GenerationType.IDENTITY )
    private Long id;

    @NotNull( message = ErrorMessages.NULL_VALUE )
    @Column(
            name = "lesson_date",
            nullable = false,
            updatable = false,
            columnDefinition = PostgreSqlFunctions.NOW + " + interval '5' day"
    )
    @Immutable
    @PartitionKey
    @FutureOrPresent( message = ErrorMessages.DATE_IS_INVALID )
    /*
    дата провидения урока
    по умолчанию ставим на 5 дней вперед
    */
    private final Date lessonDate = super.newDate(
            super.newDate().getTime() + 5 * 86400 * 1000
    );

    @Size(
            min = 5,
            max = 50,
            message = ErrorMessages.VALUE_OUT_OF_RANGE
    )
    @NotNull( message = ErrorMessages.NULL_VALUE )
    @NotBlank( message = ErrorMessages.NULL_VALUE )
    @NotEmpty( message = ErrorMessages.NULL_VALUE )
    @Column(
            length = 50,
            nullable = false,
            updatable = false,
            columnDefinition = "VARCHAR( 50 )"
    )
    @Immutable
    @PartitionKey
    private String lessonName;

    @NotNull( message = ErrorMessages.NULL_VALUE )
    @OrderBy(
            value = "createdDate DESC, comment ASC"
    )
    @Column(
            name = "comment_list"
    )
    @OneToMany(
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            targetEntity = Comment.class,
            orphanRemoval = true
    )
    @JoinColumn( name = "lesson_id" )
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
    private final List< Comment > commentList = super.newList();

    // https://www.baeldung.com/jpa-default-column-values
    @NotNull( message = ErrorMessages.NULL_VALUE )
    @Enumerated( value = EnumType.STRING )
    @Column(
            name = "lesson_status",
            nullable = false,
            columnDefinition = "VARCHAR( 255 ) DEFAULT 'CREATED'"
    )
    @PartitionKey
    private LessonStatus lessonStatus = LessonStatus.CREATED;

    public Lesson () {}
}
