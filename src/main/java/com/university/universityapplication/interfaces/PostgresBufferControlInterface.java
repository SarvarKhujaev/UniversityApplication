package com.university.universityapplication.interfaces;

public interface PostgresBufferControlInterface {
    /*
    переносим все накопленные данные
    в Буферный кэш
    */
    void insertTableContentToBuffer ();

    void calculateBufferAnalyze ();

    /*
    используется чтобы провести прогрев кэша при запуске приложения
    */
    void prewarmTable ();
}
