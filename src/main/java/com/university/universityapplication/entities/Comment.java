package com.university.universityapplication.entities;

import com.university.universityapplication.constans.PostgreSqlFunctions;
import com.university.universityapplication.constans.PostgreSqlSchema;
import com.university.universityapplication.constans.PostgreSqlTables;
import com.university.universityapplication.inspectors.TimeInspector;
import com.university.universityapplication.constans.ErrorMessages;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.PartitionKey;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Cache;

import jakarta.persistence.*;
import java.util.Date;

@Entity( name = PostgreSqlTables.COMMENTS )
@Table(
        name = PostgreSqlTables.COMMENTS,
        schema = PostgreSqlSchema.UNIVERSITY
)
@Cacheable
@Cache(
        usage = CacheConcurrencyStrategy.READ_WRITE
)
public class Comment extends TimeInspector {
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

    @OneToOne(
            fetch = FetchType.EAGER,
            cascade = CascadeType.ALL,
            targetEntity = Student.class,
            orphanRemoval = true
    )
    private Student student;
}
