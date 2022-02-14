package com.kdp.golf.user;

import com.kdp.golf.lib.LibSQL;
import com.kdp.golf.lib.Repository;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class UserRepository implements Repository<Long, User> {

    private final DataSource dataSource;
    private final Logger log = Logger.getLogger(UserRepository.class);

    public UserRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public List<User> findAll() {
        var sql = "SELECT * FROM person";
        var users = new ArrayList<User>();

        try (var connection = dataSource.getConnection();
             var statement = connection.prepareStatement(sql)) {
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

    @Override
    public Optional<User> findById(Long id) {
        var sql = "SELECT * FROM person WHERE id = ?";

        try (var connection = dataSource.getConnection();
             var statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
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

    public Optional<User> findBySessionId(String sessionId) {
        var sql = "SELECT * FROM person WHERE session_id = ?";

        try (var connection = dataSource.getConnection();
             var statement = connection.prepareStatement(sql)) {
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


    @Override
    public Optional<User> create(User u) {
        var sql = """
                INSERT INTO person (name, session_id)
                VALUES (?, ?)
                RETURNING id""";

        var name = u.name();
        var sessionId = u.sessionId();

        try (var connection = dataSource.getConnection();
             var statement = connection.prepareStatement(sql)) {
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

    @Override
    public boolean update(User user) {
        var sql = """
                UPDATE person
                SET name = ?, session_id = ?
                WHERE id = ?""";

        try (var connection = dataSource.getConnection();
             var statement = connection.prepareStatement(sql)) {
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

    @Override
    public boolean delete(Long id) {
        try (var connection = dataSource.getConnection()) {
            var sql = "DELETE FROM person WHERE id = ?";
            return LibSQL.deleteById(connection, sql, id, log);
        } catch (SQLException e) {
            log.error(e.getStackTrace());
            return false;
        }
    }
}
