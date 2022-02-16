package com.kdp.golf;

import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;

import javax.enterprise.context.ApplicationScoped;
import javax.sql.DataSource;

@ApplicationScoped
public class DatabaseConnection {

    private final Jdbi jdbi;

    public DatabaseConnection(DataSource dataSource) {
        jdbi = Jdbi.create(dataSource);
        jdbi.installPlugin(new SqlObjectPlugin());
    }

    public Jdbi jdbi() { return jdbi; }
}
