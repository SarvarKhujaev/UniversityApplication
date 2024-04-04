package com.university.universityapplication.constans.postgres_constants.postgres_partition_constants;

/*
https://rasiksuhail.medium.com/guide-to-postgresql-table-partitioning-c0814b0fbd9b
*/
public final class PostgresPartitionTypes {
    /*
    Range partitioning is a type of table partitioning where data is divided into partitions based on a specified range of values in a column.
    This is particularly useful when dealing with time-series data or any data that has a natural order.
    Each partition represents a distinct range of values, and data falling within that range is stored in that partition.
    Range partitioning allows for efficient retrieval of data within specific ranges, leading to improved query performance.
     */
    public static final String RANGE_PARTITION = "";

    public static final String LIST_PARTITION = "";

    public static final String HASH_PARTITION = "";
}
