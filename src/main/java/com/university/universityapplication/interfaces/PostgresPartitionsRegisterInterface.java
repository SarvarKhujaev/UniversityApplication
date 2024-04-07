package com.university.universityapplication.interfaces;

public interface PostgresPartitionsRegisterInterface {
    void registerPartitionsByRange ();
    void registerPartitionsByList ();
    void registerPartitionsByHash ();
}
