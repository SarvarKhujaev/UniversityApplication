package com.university.universityapplication.interfaces;

/*
хранит все методы для регистрации индексов
*/
public interface PostgresIndexesRegisterInterface {
    /*
    создает индексы
    */
    void createIndex ();

    /*
    применяет Reindex на все индексы в таблицах
    для очищения индексов от пустых и не используемых значений
    */
    void reIndex ();
}
