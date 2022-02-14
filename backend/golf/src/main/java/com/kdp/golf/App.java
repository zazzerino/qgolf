package com.kdp.golf;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.StartupEvent;
import io.quarkus.runtime.annotations.QuarkusMain;
import org.jboss.logging.Logger;

import javax.enterprise.event.Observes;
import javax.sql.DataSource;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.stream.Collectors;

@QuarkusMain
public class App {

    private final DataSource dataSource;
    private final Logger log = Logger.getLogger(App.class);

    public App(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public static void main(String... args) {
        Quarkus.run(args);
    }

    void initDatabase(@Observes StartupEvent _e) throws IOException, SQLException {
        log.info("initializing database...");
        var schemaPath = Paths.get("src/main/resources/schema.sql");

        try (var connection = dataSource.getConnection();
             var statement = connection.createStatement();
             var lines = Files.lines(schemaPath);) {
            var sql = lines.collect(Collectors.joining("\n"));
            statement.execute(sql);
        }
    }
}
