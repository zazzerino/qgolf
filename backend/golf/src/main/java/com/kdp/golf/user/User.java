package com.kdp.golf.user;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.sql.ResultSet;
import java.sql.SQLException;

@Value.Immutable
@JsonSerialize(as = ImmutableUser.class)
public abstract class User {

    @Value.Parameter
    public abstract Long id();
    @Value.Parameter
    public abstract String name();
    @Value.Parameter
    public abstract String sessionId();

    public static final String DEFAULT_NAME = "anon";

    public static class Mapper implements RowMapper<User> {
        @Override
        public User map(ResultSet rs, StatementContext ctx) throws SQLException {
            var id = rs.getLong("id");
            var name = rs.getString("name");
            var sessionId = rs.getString("session_id");
            return ImmutableUser.of(id, name, sessionId);
        }
    }
}
