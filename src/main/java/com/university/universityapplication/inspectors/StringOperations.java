package com.university.universityapplication.inspectors;

import com.university.universityapplication.constans.postgres_constants.PostgreSqlSchema;

public class StringOperations extends CollectionsInspector {
    protected StringOperations () {}

    protected String generateCacheName () {
        return String.join(
                ".",
                "hibernate.cache",
                PostgreSqlSchema.UNIVERSITY.toLowerCase()
        );
    }
}
