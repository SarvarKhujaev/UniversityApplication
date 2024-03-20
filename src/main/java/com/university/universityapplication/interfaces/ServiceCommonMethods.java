package com.university.universityapplication.interfaces;

public interface ServiceCommonMethods {
    default void close( final Throwable throwable ) {}

    void close();
}
