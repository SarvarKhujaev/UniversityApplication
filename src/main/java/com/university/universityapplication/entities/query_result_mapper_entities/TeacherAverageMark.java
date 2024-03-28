package com.university.universityapplication.entities.query_result_mapper_entities;

public final class TeacherAverageMark {
    private Long lessonCount;
    private float averageMark;

    private String averageMarkNaming;

    public TeacherAverageMark(
            final float averageMark,
            final Long lessonCount,
            final String averageMarkNaming
    ) {
        this.setAverageMark( averageMark );
        this.setLessonCount( lessonCount );
        this.setAverageMarkNaming( averageMarkNaming );
    }

    public float getAverageMark() {
        return this.averageMark;
    }

    public Long getLessonCount() {
        return this.lessonCount;
    }

    public String getAverageMarkNaming() {
        return this.averageMarkNaming;
    }

    public void setAverageMark( final float averageMark ) {
        this.averageMark = averageMark;
    }

    public void setLessonCount( final Long lessonCount ) {
        this.lessonCount = lessonCount;
    }

    public void setAverageMarkNaming( final String averageMarkNaming ) {
        this.averageMarkNaming = averageMarkNaming;
    }

    @Override
    public String toString () {
        return this.getAverageMarkNaming()
                + " : "
                + this.getAverageMark()
                + " : "
                + this.getLessonCount();
    }
}
