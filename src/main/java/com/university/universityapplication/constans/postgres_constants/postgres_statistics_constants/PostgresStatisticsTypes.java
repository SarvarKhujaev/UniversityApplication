package com.university.universityapplication.constans.postgres_constants.postgres_statistics_constants;

public enum PostgresStatisticsTypes {
    DEPENDENCIES,
    NDISTINCT,

    /*
    https://www.postgresql.org/docs/14/multivariate-statistics-examples.html
    https://www.postgresql.org/docs/14/monitoring-stats.html#WAIT-EVENT-TABLE
    https://www.postgresql.org/docs/14/monitoring-stats.html#WAIT-EVENT-ACTIVITY-TABLE

    Необходимо заметить, что можно создавать мультивариативную
    коррелированную статистику (MCV), но естественно за поддержку такой
    статистики нужно платить производительностью при перестроении статистики.
    Более подробно можно почитать по ссылке
    */
    MCV,
}
