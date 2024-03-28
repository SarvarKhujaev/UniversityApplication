package com.university.universityapplication;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith( value = Suite.class )
@Suite.SuiteClasses( value = {
        DatabaseAvailabilityTest.class,
} )
public final class JavaTest {}
