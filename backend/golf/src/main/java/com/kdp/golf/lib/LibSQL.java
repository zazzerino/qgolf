package com.kdp.golf.lib;

import org.jboss.logging.Logger;

import java.sql.Connection;
import java.sql.SQLException;

public class LibSQL {

    public static boolean deleteById(Connection connection, String sql, Long id, Logger log) {
        try (var statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            var rowsUpdated = statement.executeUpdate();
            return rowsUpdated == 1;
        } catch (SQLException e) {
            log.error(e.getStackTrace());
            return false;
        }
    }
}
