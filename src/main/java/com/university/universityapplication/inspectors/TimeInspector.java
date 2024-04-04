package com.university.universityapplication.inspectors;

import com.university.universityapplication.constans.entities_constants.LessonAppearanceTypes;

import java.util.Calendar;
import java.util.List;
import java.util.Date;

import java.time.Month;
import java.time.Year;

public class TimeInspector extends StringOperations {
    private final Calendar calendar = Calendar.getInstance();

    private Calendar getCalendar() {
        return this.calendar;
    }

    protected TimeInspector () {}

    protected final Date newDate () {
        return new Date();
    }

    protected final Date newDate (
            final long timeInterval
    ) {
        return new Date( timeInterval );
    }

    /*
    когда студент присоединяеться к занятию,
    проверяем пришел ли он вовремя или опоздал
     */
    protected final LessonAppearanceTypes getLessonAppearanceTypesDueToAppearanceTime (
            final Date studentAppearanceDate,
            final Date lessonStartDate
    ) {
        return studentAppearanceDate.after( lessonStartDate )
                ? LessonAppearanceTypes.LATE
                : LessonAppearanceTypes.IN_TIME;
    }

    protected final synchronized int getCurrentYear () {
        return Year.now().getValue();
    }

    protected final synchronized List< Month > getListOfMonths () {
        return super.newList( Month.values() );
    }

    protected final synchronized Date getMonthStartOrEnd (
            final boolean flag,
            final Month month
    ) {
        this.getCalendar().set( Calendar.YEAR, Year.now().getValue() );
        this.getCalendar().set( Calendar.DAY_OF_MONTH, 1 );
        this.getCalendar().set(
                Calendar.MONTH,
                flag ? ( month.getValue() - 1 ) : month.getValue()
        );

        return this.getCalendar().getTime();
    }
}
