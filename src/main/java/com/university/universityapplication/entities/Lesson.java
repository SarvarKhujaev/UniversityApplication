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

@Entity( name = PostgreSqlTables.LESSONS )
@Table(
        name = PostgreSqlTables.LESSONS,
        schema = PostgreSqlSchema.UNIVERSITY
)
@Cacheable
@Cache(
        usage = CacheConcurrencyStrategy.READ_ONLY
)
public class Lesson extends TimeInspector {
    @Id
    @GeneratedValue(  strategy = GenerationType.IDENTITY )
    private Long id;

    @NotNull( message = ErrorMessages.NULL_VALUE )
    @Column(
            name = "lesson_date",
            nullable = false,
            updatable = false,
            columnDefinition = PostgreSqlFunctions.NOW + " interval '5' day"
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
            columnDefinition = "VARCHAR( 50 ) NOT NULL"
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
    private final List< Comment > commentList = super.newList();
}
