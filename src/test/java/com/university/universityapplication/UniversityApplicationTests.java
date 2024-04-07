package com.university.universityapplication;

import com.university.universityapplication.inspectors.LogInspector;
import org.springframework.boot.test.context.SpringBootTest;

import org.junit.jupiter.api.Test;
import org.junit.runner.JUnitCore;

import junit.framework.JUnit4TestAdapter;
import junit.extensions.RepeatedTest;

@SpringBootTest
public final class UniversityApplicationTests {
    @Test
    void contextLoads() {
        /*
        запускаем тесты
        */
        new LogInspector(
                new JUnitCore().run(
                        new RepeatedTest(
                                new JUnit4TestAdapter( JavaTest.class ), 3
                        )
                )
        );
    }
}
