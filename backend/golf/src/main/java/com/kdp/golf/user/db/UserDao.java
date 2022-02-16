package com.kdp.golf.user.db;

import com.kdp.golf.user.User;
import org.jdbi.v3.sqlobject.config.RegisterRowMapper;
import org.jdbi.v3.sqlobject.customizer.BindMethods;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;
import java.util.Optional;

/**
 * This class provides access to the `person` table.
 * The name `person` is used because `user` would conflict with a builtin postgres table.
 */
public interface UserDao {

    @SqlQuery("SELECT * FROM person")
    @RegisterRowMapper(UserMapper.class)
    List<User> findAll();

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
