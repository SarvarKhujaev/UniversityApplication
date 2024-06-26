package com.university.universityapplication.database;

import com.university.universityapplication.constans.postgres_constants.postgres_materialized_view_constants.PostgresMaterializedViewMethods;
import com.university.universityapplication.interfaces.PostgresMaterializedViewRegisterInterface;
import com.university.universityapplication.constans.postgres_constants.PostgresCreateValues;
import com.university.universityapplication.constans.postgres_constants.PostgreSqlSchema;
import com.university.universityapplication.inspectors.LogInspector;

import org.hibernate.Session;

public final class PostgresMaterializedViewRegister extends LogInspector implements PostgresMaterializedViewRegisterInterface {
    private final Session session;

    private Session getSession() {
        return this.session;
    }

    public static PostgresMaterializedViewRegister generate (
            final Session session
    ) {
        return new PostgresMaterializedViewRegister( session );
    }

    private PostgresMaterializedViewRegister (
            final Session session
    ) {
        this.session = session;
    }

    @Override
    public void createAllMaterializedViews () {
        super.analyze(
                super.getAllMaterializedViewsNames(),
                viewName -> super.logging(
                        this.getSession().createNativeQuery( viewName ).getQueryString()
                )
        );
    }

    @Override
    public void refreshAllViews () {
        super.analyze(
                this.getSession().createNativeQuery(
                        PostgresMaterializedViewMethods.REFRESH_ALL_MATERIALIZED_VIEWS.formatted(
                                PostgresCreateValues.MATERIALIZED_VIEW.getOriginalValue(),
                                PostgreSqlSchema.UNIVERSITY
                        ),
                        String.class
                ).getResultList(),
                viewName -> this.getSession().createNativeQuery(
                        PostgresMaterializedViewMethods.REFRESH.formatted(
                                viewName
                        )
                )
        );
    }
}
