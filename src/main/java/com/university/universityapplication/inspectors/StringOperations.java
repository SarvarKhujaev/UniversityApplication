package com.university.universityapplication.inspectors;

import java.time.Month;

public class StringOperations extends CollectionsInspector {
    protected StringOperations () {}

    protected final synchronized String getYearAndMonthConvertedValue (
            final String tableName,
            final Month month,
            final int year
    ) {
        return String.join(
                "_",
                tableName,
                String.valueOf( year ),
                month.name()
        );
    }
}
