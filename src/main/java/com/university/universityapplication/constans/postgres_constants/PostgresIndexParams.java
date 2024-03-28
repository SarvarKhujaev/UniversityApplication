package com.university.universityapplication.constans.postgres_constants;

/*
хранит все кинфигурации для индексов PostgreSQL
 */
public final class PostgresIndexParams {
    /*
    CREATE [ UNIQUE ] INDEX [ CONCURRENTLY ] [ [ IF NOT EXISTS ] name ]
    ON [ ONLY ] table_name [ USING method ]
    ( { column_name | ( expression ) } [ COLLATE collation ] [ opclass [ (
    opclass_parameter = value [, ... ] ) ] ] [ ASC | DESC ] [ NULLS { FIRST |
    LAST } ] [, ...] )
    [ INCLUDE ( column_name [, ...] ) ]
    [ WITH ( storage_parameter [= value] [, ... ] ) ]
    [ TABLESPACE tablespace_name ]
    [ WHERE predicate ]
    */
    public static final String CREATE_INDEX = "CREATE INDEX IF NOT EXISTS %s";

    /*
    225
    Рассмотрим основные параметры:
    UNIQUE - указывает, что при создании индекса (если в таблице уже есть
    данные) и при каждом добавлении или изменении данных, при которых будет
    нарушена уникальность индекса, будет выдана ошибка.
    */
    public static final String UNIQUE = "UNIQUE";

    /*
    CONCURRENTLY - конкурентное построение индекса, без блокировок,
    которые бы предотвращали добавление, изменение или удаление записей в
    таблице. Стандартная операция построения индекса блокирует запись (но не
    чтение) данных в таблице до своего завершения
     */
    public static final String CONCURRENTLY = "CONCURRENTLY";

    /*
    INCLUDE - позволяет указать список столбцов, которые войдут в индекс
    как неключевые столбцы. Эти столбцы не будут использоваться в условии
    поиска для сканирования по индексу.

    !!! Преимущество данной опции - при сканировании только индекса,
        содержимое неключевых столбцов может быть получено без обращения к
        целевой таблице, так как оно находится непосредственно в элементе
        индекса. Например, таким образом запрос из предыдущей главы мы бы
        ускорили в десятки раз!!!
    */
    public static final String INCLUDE = "INCLUDE";

    /*
    name - имя создаваемого индекса. Указание схемы при этом не
    допускается: индекс всегда относится к той же схеме, что и родительская
    таблица. Если имя не указано, Постгрес сам формирует составное имя,
    состоящее из имени таблицы и имён индексируемых столбцов
     */
    public static final String NAME = "NAME";

    /*
    ONLY - указывает, что индексы не должны рекурсивно создаваться в
    секциях секционированной таблицы. Default - выполняется рекурсивно.
     */
    public static final String ONLY = "ONLY";

    /*
    table_name - имя индексируемой таблицы
     */
    public static final String TABLE_NAME = "TABLE_NAME";

    /*
    column_name - имя столбца таблицы.
     */
    public static final String COLUMN_NAME = "COLUMN_NAME";

    /*
    tablespace_name - табличное пространство, в котором будет создан
    индекс. Default - выбирается табличное пространство заданное по умолчанию
    для данной БД.
     */
    public static final String TABLESPACE_NAME = "TABLESPACE_NAME";

    /*
    method - имя применяемого метода индекса. Возможные варианты: btree,
    hash, gist, spgist, gin и brin (подробнее рассмотрим дальше). Default btree
     */
    public static final String METHOD = "METHOD";

    /*
    expression - выражение с одним или несколькими столбцами таблицы.
    Обычно выражение должно записываться в скобках, как показано в синтаксисе
    команды. Однако скобки можно опустить, если выражение записано в виде
    вызова функции.
    */
    public static final String EXPRESSION = "EXPRESSION";

    /*
    ASC / DESC - указывает порядок сортировки. Default - по возрастанию.
     */
    public static final String ASC = "ASC";

    public static final String DESC = "DESC";

    /*
    predicate - выражение ограничения для частичного индекса.
     */
    public static final String PREDICATE = "PREDICATE";

    /*
    fillfactor - фактор заполнения для индекса определяется в процентном
    отношении, насколько плотно метод индекса будет заполнять страницы индекса.
    Для Btree концевые страницы заполняются до этого процента при начальном
    построении индекса и позже, при расширении индекса вправо (добавлении
    новых наибольших значений ключа). Меняйте только в том случае, если чётко
    понимаете, что делаете.
    */
    public static final String FILL_FACTOR = "FILLFACTOR";

    /*
    REINDEX INDEX CONCURRENTLY table_name
    */
    public static final String REINDEX = "REINDEX";
}
