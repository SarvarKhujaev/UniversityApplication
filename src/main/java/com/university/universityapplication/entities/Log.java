//package com.university.universityapplication.entities;
//
//import com.university.universityapplication.constans.postgres_constants.PostgreSqlFunctions;
//import com.university.universityapplication.constans.postgres_constants.PostgreSqlTables;
//import com.university.universityapplication.inspectors.TimeInspector;
//import com.university.universityapplication.constans.entiti_constants.ErrorMessages;
//
//import jakarta.validation.constraints.NotNull;
//import jakarta.persistence.*;
//
//import org.hibernate.annotations.PartitionKey;
//import org.hibernate.annotations.Immutable;
//
//import java.util.Date;
//
//@Entity( name = PostgreSqlTables.LOG )
//@Table( name = PostgreSqlTables.LOG )
//public final class Log extends TimeInspector {
//    @Id
//    @GeneratedValue(  strategy = GenerationType.IDENTITY )
//    private Long id;
//
//    @Immutable
//    @PartitionKey
//    @NotNull( message = ErrorMessages.NULL_VALUE )
//    @Column(
//            name = "created_date",
//            nullable = false,
//            updatable = false,
//            columnDefinition = PostgreSqlFunctions.NOW
//    )
//    private final Date createdDate = super.newDate(); // дата создания
//}
