package com.university.universityapplication.inspectors;

import com.university.universityapplication.constans.entities_constants.LessonAppearanceTypes;
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

    /*
    когда студент присоединяеться к занятию,
    проверяем пришел ли он вовремя или опоздал
     */
    protected LessonAppearanceTypes getLessonAppearanceTypesDueToAppearanceTime (
            final Date studentAppearanceDate,
            final Date lessonStartDate
    ) {
        return studentAppearanceDate.after( lessonStartDate )
                ? LessonAppearanceTypes.LATE
                : LessonAppearanceTypes.IN_TIME;
    }
}
