package com.university.universityapplication.constans.postgres_constants.postgres_statistics_constants;

/*
https://habr.com/ru/companies/postgrespro/articles/576100/

https://www.postgresql.org/docs/14/multivariate-statistics-examples.html
*/
public final class PostgresStatisticsParams {
    /*
    CREATE STATISTICS stts (dependencies, ndistinct) ON a, b FROM t;
    */
    public static final String CREATE_STATISTICS = "CREATE STATISTICS %s ( %s ) ON %s FROM %s;";

    /*
    track_activities включает мониторинг текущих команд,
    выполняемых любым серверным процессом
    */
    public static final String TRACK_ACTIVITIES = "track_activities";

    /*
    track_functions включает отслеживание использования
    пользовательских функций
    */
    public static final String TRACK_FUNCTIONS = "track_functions";

    /*
    track_io_timing включает мониторинг времени чтения и записи блоков
    */
    public static final String TRACK_IO_TIMING = "track_io_timing";

    /*
    track_counts определяет необходимость сбора статистики по
    обращениям к таблицам и индексам
    */
    public static final String TRACK_COUNTS = "track_counts";

    /*
    pg_stat_database
    Отсюда можем получить следующую информацию:
        ● как много информации получаем из кэша
        ● как часто бывают проблемы с транзакциями
        Основные колонки:
        ● blks_hit - количество блоков, полученных из кэша Постгреса
        ● blks_read - количество блоков, прочитанных с диска
        ● xact_commit - количество закоммиченных транзакций
        ● xact_rollback - количество транзакций, где был выполнен откат транзакции

    SELECT * FROM pg_stat_database WHERE datname = 'demo';
     */
    public static final String PG_STAT_DATABASE = "pg_stat_database";

    /*
    pg_stats
    Хранит гистограмму распределения значений в таблице и используется
    планировщиком для построения более оптимального плана и резервирования
    памяти исходя из предполагаемого количества возвращаемых строк.
    Также здесь хранится частота, с которой в таблице встречаются значения NULL.

    Основные колонки:
        ● schemaname - имя схемы, содержащей таблицу
        (pg_namespace.nspname)
        ● tablename - имя таблицы (pg_class.relname)
        ● attname - имя столбца, описываемого этой строкой
        (pg_attribute.attname)
        ● null_frac - доля записей, в которых этот столбец содержит NULL
        ● n_distinct - число больше нуля представляет примерное
        количество различных значений в столбце.
        ● most_common_vals - список самых частых значений в столбце
        (NULL, если не находятся значения, встречающиеся чаще других)
        ● most_common_freqs - список частот самых частых значений, то
        есть число их вхождений, разделённое на общее количество строк
        ● histogram_bounds - список значений, разделяющих значения
        столбца на примерно одинаковые популяции.
        ● correlation - статистическая корреляция между физическим
        порядком строк и логическим порядком значений столбца.
        Допустимые значения лежат в диапазоне -1 .. +1. Когда значение
        около -1 или +1, сканирование индекса по столбцу будет считаться
        дешевле, чем когда это значение около нуля, как результат
        уменьшения случайного доступа к диску (этот столбец содержит
        NULL, если для типа данных столбца не определён оператор <)

        SELECT * FROM pg_stats WHERE tablename = 'flights'
     */
    public static final String PG_STATS = "pg_stats";

    /*
    pg_statistic_ext
    Предназначено для хранения статистики для связанных столбцов - когда
    мы в запросе используем несколько атрибутов и при этом их значения
    коррелированы.
     */
    public static final String PG_STATISTICS_EXT = "pg_statistic_ext";

    /*
    https://www.postgresql.org/docs/14/multivariate-statistics-examples.html
    https://www.postgresql.org/docs/14/monitoring-stats.html#WAIT-EVENT-TABLE

    pg_stat_activity

    Содержит в себе по одной строке на каждый процесс сервера. Напомню,
    что любое подключение к серверу порождает форк основного процесса и также
    отображается в этом представлении.

    Основные параметры:
        ● datid - OID базы данных, к которой подключён этот серверный процесс
        ● datname - имя базы данных, к которой подключён этот серверный процесс
        ● pid - идентификатор процесса этого серверного процесса

        ● backend_start - время запуска процесса.
        Для процессов, обслуживающих клиентов, это время подключения клиента к серверу

        ● client_addr - IP-адрес клиента, подключённого к этому серверному
        процессу. Значение null в этом поле означает, что клиент
        подключён через сокет Unix на стороне сервера или что это
        внутренний процесс, например, автоочистка

        ● client_hostname - имя компьютера для подключённого клиента.
        Это поле будет отлично от null только в случае соединений по IP и
        только при включённом режиме log_hostname

        ● wait_event_type - тип события169, которого ждёт процесс

        ● wait_event - имя ожидаемого события170, если процесс находится в состоянии ожидания

        ● state - общее текущее состояние этого серверного процесса.

        Возможные значения:
            ○ active - серверный процесс выполняет запрос
            ○ idle - серверный процесс ожидает новой команды от клиента
            ○ idle in transaction - серверный процесс находится внутри
            транзакции, но в настоящее время не выполняет ничего

            ○ idle in transaction (aborted) - состояние подобно idle in transaction,
                за исключением того, что один из операторов в
                транзакции вызывал ошибку

            ○ fastpath function call - серверный процесс выполняет fast-path функцию

            ○ disabled - отображается для серверных процессов, у которых параметр track_activities отключён

            ● query - текст последнего запроса этого серверного процесса.
            По умолчанию текст запроса обрезается до 1024 байт (параметр track_activity_query_size)

            ● backend_type - тип текущего серверного процесса

       SELECT * FROM pg_stat_activity;
     */
    public static final String PG_STAT_ACTIVITY = "pg_stat_activity";

    /*
    pg_stat_user_tables
    Здесь собрана статистика, что вообще у нас происходит с таблицей.
    Сколько строк, мёртвых строк и т.д.

    Основные параметры:
        ● relid - OID таблицы
        ● schemaname - имя схемы, в которой расположена эта таблица
        ● relname - имя таблицы
        ● seq_scan - количество последовательных чтений, произведённых в этой таблице
        ● seq_tup_read - количество "живых" строк, прочитанных при последовательных чтениях
        ● n_tup_upd - количество изменённых строк
        ● n_live_tup - оценочное количество "живых" строк
        ● n_dead_tup - оценочное количество "мертвых" строк
        ● last_autovacuum - время последней очистки таблицы фоновым процессом автоочистки

    SELECT * FROM pg_stat_user_tables WHERE relname = 'flights'
     */
    public static final String PG_STAT_USER_TABLES = "pg_stat_user_tables";

    /*
    pg_stat_user_indexes
    Здесь собирается статистика по используемым индексам.

    Основные параметры:
        ● relid - OID таблицы для индекса
        ● relname - имя таблицы для индекса
        ● idx_scan - количество произведённых сканирований по этому индексу
        ● indexrelid - OID индекса
        ● indexrelname - имя индекса
        ● idx_tup_read - количество элементов индекса, возвращённых при сканированиях по этому индексу
        ● idx_tup_fetch - количество живых строк таблицы, отобранных при простых сканированиях по этому индексу
     */
    public static final String PG_STAT_USER_INDEXES = "pg_stat_user_tables";

    /*
    pg_stat_statements

    Модуль pg_stat_statements предоставляет возможность отслеживать
    статистику планирования и выполнения сервером всех операторов SQL.
    Этот модуль нужно загружать, добавив pg_stat_statements в
    shared_preload_libraries в файле postgresql.conf, так как ему требуется
    дополнительная разделяемая память. Это значит, что для загрузки или выгрузки
    модуля необходимо перезапустить сервер.
    Когда модуль pg_stat_statements загружается, он отслеживает
    статистику по всем базам данных на сервере. Для получения и обработки этой
    статистики этот модуль предоставляет представление pg_stat_statements и
    вспомогательные функции pg_stat_statements_reset и pg_stat_statements. Эти
    объекты не доступны глобально, но их можно установить в определённой базе
    данных, выполнив команду CREATE EXTENSION pg_stat_statements.

    Основные параметры:
        ● rows - суммарное количество возвращённых строк

        ● shared_blks_hit - количество страниц, которые были в кэше БД

        ● shared_blks_read - количество страниц, которые были прочитаны с
        диска, чтобы выполнить запросы такого типа

        ● shared_blks_dirtied - количество страниц, которые были изменены

        ● shared_blks_written - количество страниц, которые были записаны
        на диск

        ● local_blks_hit, local_blks_read, local_blks_dirtied,
        local_blks_written - то же самое, что предыдущие четыре, только
        для временных таблиц и индексов

        ● temp_blks_read - сколько страниц временных данных было
        прочитано

        ● temp_blks_written - сколько страниц временных данных было
        записано (используется при сортировке на диске, когда она не
        помещается в память и других временных операциях)

        ● blk_read_time - сколько времени суммарно заняло чтение с диска

        ● blk_write_time - сколько времени суммарно заняла запись на диск
    */
    public static final String PG_STAT_STATEMENTS = "pg_stat_statements";
}
