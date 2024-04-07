package com.university.universityapplication.interfaces;

/*
хранит методы для создания расширений в PostgreSql
 */
public interface PostgresExtensionsRegisterInterface {
    /*
    создает расширение в БД
    */
    void createExtension();
}
