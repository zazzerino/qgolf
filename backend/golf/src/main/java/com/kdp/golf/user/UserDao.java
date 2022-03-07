package com.kdp.golf.user;

import org.jdbi.v3.sqlobject.config.RegisterRowMapper;
import org.jdbi.v3.sqlobject.customizer.BindMethods;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.Optional;

public interface UserDao {

    @SqlQuery("SELECT * FROM person WHERE id = ?")
    @RegisterRowMapper(UserMapper.class)
    Optional<User> findById(Long id);

    @SqlQuery("SELECT * FROM person WHERE session_id = ?")
    @RegisterRowMapper(UserMapper.class)
    Optional<User> findBySessionId(String sessionId);

    @SqlUpdate("INSERT INTO person (name, session_id) VALUES (?, ?)")
    @GetGeneratedKeys("id")
    Long create(String name, String sessionId);

    @SqlUpdate("""
        UPDATE person
        SET name = :name, session_id = :sessionId
        WHERE id = :id""")
    void update(@BindMethods User user);

    @SqlUpdate("DELETE FROM person WHERE id = ?")
    void delete(Long id);
}
