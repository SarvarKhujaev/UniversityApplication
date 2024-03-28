package com.university.universityapplication.interfaces;

public interface PostgresBufferControlInterface {
    /*
    переносим все накопленные данные
    в Буферный кэш
    */
    void insertTableContentToBuffer ();

    void prewarmTable ();
}
