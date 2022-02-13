package com.kdp.golf.user;

import com.kdp.golf.Lib;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class UserDao {

    private final Logger log = Logger.getLogger(UserDao.class);

    public List<User> findAll(Connection connection) {
        var sql = "SELECT * FROM person";
        var users = new ArrayList<User>();

        try (var statement = connection.prepareStatement(sql)) {
            var resultSet = statement.executeQuery();

            while (resultSet.next()) {
                var user = User.from(resultSet);
                users.add(user);
            }

            return users;
        } catch (SQLException e) {
            log.error(e.getStackTrace());
        }

        return users;
    }

    public Optional<User> findById(Connection connection, Long userId) {
        var sql = "SELECT * FROM person WHERE id = ?";

        try (var statement = connection.prepareStatement(sql)) {
            statement.setLong(1, userId);
            var resultSet = statement.executeQuery();

            if (resultSet.next()) {
                var user = User.from(resultSet);
                return Optional.of(user);
            }
        } catch (SQLException e) {
            log.error(e.getStackTrace());
        }

        return Optional.empty();
    }

    public Optional<User> findBySessionId(Connection connection, String sessionId) {
        var sql = "SELECT * FROM person WHERE session_id = ?";

        try (var statement = connection.prepareStatement(sql)) {
            statement.setString(1, sessionId);
            var resultSet = statement.executeQuery();

            if (resultSet.next()) {
                var user = User.from(resultSet);
                return Optional.of(user);
            }
        } catch (SQLException e) {
            log.error(e.getStackTrace());
        }

        return Optional.empty();
    }

    public Optional<String> findName(Connection connection, Long userId) {
        return findById(connection, userId)
                .map(User::name);
    }

    public Optional<User> create(Connection connection, String name, String sessionId) {
        var sql = """
                INSERT INTO person (name, session_id)
                VALUES (?, ?)
                RETURNING id""";

        try (var statement = connection.prepareStatement(sql)) {
            statement.setString(1, name);
            statement.setString(2, sessionId);
            var resultSet = statement.executeQuery();

            if (resultSet.next()) {
                var id = resultSet.getLong("id");
                var user = new User(id, name, sessionId);
                return Optional.of(user);
            }
        } catch (SQLException e) {
            log.error(e.getStackTrace());
        }

        return Optional.empty();
    }

    public boolean update(Connection connection, User user) {
        var sql = """
                UPDATE person
                SET name = ?, session_id = ?
                WHERE id = ?""";

        try (var statement = connection.prepareStatement(sql)) {
            statement.setString(1, user.name());
            statement.setString(2, user.sessionId());
            statement.setLong(3, user.id());

            var rowsUpdated = statement.executeUpdate();
            return rowsUpdated == 1;
        } catch (SQLException e) {
            log.error(e.getStackTrace());
            return false;
        }
    }

    public boolean delete(Connection connection, Long id) {
        var sql = "DELETE FROM person WHERE id = ?";
        return Lib.deleteById(connection, sql, id, log);
    }
}
