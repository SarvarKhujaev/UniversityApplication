package com.university.universityapplication.entities;

import com.university.universityapplication.constans.postgres_constants.PostgreSqlSchema;
import com.university.universityapplication.constans.postgres_constants.PostgreSqlTables;
import com.university.universityapplication.constans.entities_constants.ErrorMessages;

import org.hibernate.annotations.PartitionKey;
import org.hibernate.annotations.Immutable;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.persistence.*;

/*
название направления по которым проводятся занятия
*/
@Entity( name = PostgreSqlTables.EDUCATION_DIRECTIONS )
@Table(
        name = PostgreSqlTables.EDUCATION_DIRECTIONS,
        schema = PostgreSqlSchema.UNIVERSITY
)
public final class EducationDirection {
    public long getId() {
        return this.id;
    }

    public String getDirectionName() {
        return this.directionName;
    }

    public void setDirectionName ( final String directionName ) {
        this.directionName = directionName;
    }

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private long id;

    @Size(
            min = 5,
            max = 50,
            message = ErrorMessages.VALUE_OUT_OF_RANGE
    )
    @Immutable
    @PartitionKey
    @NotNull( message = ErrorMessages.NULL_VALUE )
    @Column(
            name = "direction_name",
            unique = true,
            nullable = false,
            updatable = false,
            columnDefinition = "VARCHAR( 50 )"
    )
    private String directionName;

    public EducationDirection () {}
}
