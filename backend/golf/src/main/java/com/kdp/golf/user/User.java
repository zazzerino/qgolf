package com.kdp.golf.user;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * If a user has not been saved to the database, they will have an id of -1.
 */

public record User(Long id,
                   String name,
                   String sessionId) {

    public static final String DEFAULT_NAME = "anon";

    public static User of(String name, String sessionId) {
        return new User(-1L, name, sessionId);
    }

    public static User from(ResultSet rs) throws SQLException {
        var id = rs.getLong("id");
        var name = rs.getString("name");
        var sessionId = rs.getString("session_id");

        return new User(id, name, sessionId);
    }

    public User withName(String name) {
        return new User(id, name, sessionId);
    }

    @Override
    @JsonIgnore
    public String sessionId() { return sessionId; }
}
