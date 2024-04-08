package com.university.universityapplication.interfaces;

import java.util.List;

public interface PostgresFunctionsRegisterInterface {
    void createAllFunctions();

    List< String > getListOfDbTables ();

    List< String > getListOfIndexesInTable (
            final String schemaName,
            final String tableName
    );
}
