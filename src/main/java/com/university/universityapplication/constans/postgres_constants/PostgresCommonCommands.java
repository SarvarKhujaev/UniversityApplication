package com.university.universityapplication.constans.postgres_constants;

/*
хранит все основные и часто используемые запрос в БД
*/
/*
Общий синтаксис для создания секционированной таблицы:
    CREATE TABLE
    ...
    PARTITION BY { RANGE | LIST | HASH } ( { имя_столбца | ( выражение ) } [
    COLLATE правило_сортировки ][, ... ][класс_операторов] )

Задаваемый в скобках список столбцов или выражений формирует ключ разбиения таблицы.
Для разбиения по диапазонам или по хэшу (очень редко используется)
ключ разбиения может включать несколько столбцов или выражений, но для
разбиения по спискам ключ должен состоять из одного столбца или выражения.
Секционируемая таблица разделяется на подтаблицы (называемые секциями),
которые создаются отдельными командами CREATE TABLE.
Сама по себе секционированная таблица не содержит данных.
Строка данных, вставляемая в эту таблицу, перенаправляется в секцию в зависимости от
значений столбцов или выражений в ключе разбиения.
Если значениям в новой строке не соответствует ни одна из существующих секций, возникает ошибка.

CREATE TABLE measurement (
    city_id         serial not null,
    logdate         date not null default now(),
    peaktemp        int,
    unitsales       int
) PARTITION BY range ( logdate );

CREATE TABLE IF NOT EXISTS TEST_2024_APRIL PARTITION OF measurement FOR VALUES FROM ( 'Mon Apr 01 16:50:33 UZT 2024' ) TO ( 'Wed May 01 16:50:33 UZT 2024' );

CREATE INDEX ON measurement USING BTree(logdate);

INSERT INTO measurement ( peaktemp, unitsales ) VALUES ( 1, 2 );
INSERT INTO measurement ( peaktemp, unitsales ) VALUES ( 1, 2 );
INSERT INTO measurement ( peaktemp, unitsales ) VALUES ( 1, 2 );
INSERT INTO measurement ( peaktemp, unitsales ) VALUES ( 1, 2 );
INSERT INTO measurement ( peaktemp, unitsales ) VALUES ( 1, 2 );
INSERT INTO measurement ( peaktemp, unitsales ) VALUES ( 1, 2 );
INSERT INTO measurement ( peaktemp, unitsales ) VALUES ( 1, 2 );
INSERT INTO measurement ( peaktemp, unitsales ) VALUES ( 1, 2 );
INSERT INTO measurement ( peaktemp, unitsales ) VALUES ( 1, 2 );
INSERT INTO measurement ( peaktemp, unitsales ) VALUES ( 1, 2 );

SELECT count(*) from TEST_2024_APRIL;
SELECT count(*) from measurement;
*/
public final class PostgresCommonCommands {
    public static final String SELECT_FROM = "SELECT * FROM";

    public static final String CREATE = "CREATE %s IF NOT EXISTS";

    /*
    Range partitioning is a type of table partitioning where data is divided into partitions based on a specified range of values in a column.
    This is particularly useful when dealing with time-series data or any data that has a natural order.
    Each partition represents a distinct range of values, and data falling within that range is stored in that partition.
    Range partitioning allows for efficient retrieval of data within specific ranges, leading to improved query performance.
    */
    public static final String RANGE_PARTITIONING = "%s PARTITION OF %s.%s FOR VALUES FROM ( '%s' ) TO ( '%s' );";
}
