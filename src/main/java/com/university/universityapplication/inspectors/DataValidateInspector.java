package com.university.universityapplication.inspectors;

import com.university.universityapplication.UniversityApplication;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;

import java.util.Set;

public class DataValidateInspector {
    protected DataValidateInspector () {}

    protected boolean objectIsNotNull (
            final Object o
    ) {
        return o != null;
    }

    protected <T> Set< ConstraintViolation< T > > checkEntityValidation (
            final Validator validator,
            final T object
    ) {
        return validator.validate( object );
    }

    /*
    получает в параметрах название параметра из файла application.yaml
    проверят что context внутри main класса GpsTabletsServiceApplication  инициализирован
    и среди параметров сервиса сузествует переданный параметр
    */
    protected final synchronized <T> T checkContextOrReturnDefaultValue (
            final String paramName,
            final T defaultValue
    ) {
        return this.objectIsNotNull( UniversityApplication.context )
                && this.objectIsNotNull(
                UniversityApplication
                        .context
                        .getEnvironment()
                        .getProperty( paramName )
        )
                ? (T) UniversityApplication
                .context
                .getEnvironment()
                .getProperty( paramName )
                : defaultValue;
    }
}