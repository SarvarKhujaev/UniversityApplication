package com.university.universityapplication.inspectors;

import java.util.Date;

public class TimeInspector extends StringOperations {
    protected TimeInspector () {}

    protected Date newDate () {
        return new Date();
    }

    protected Date newDate (
            final long timeInterval
    ) {
        return new Date( timeInterval );
    }
}
