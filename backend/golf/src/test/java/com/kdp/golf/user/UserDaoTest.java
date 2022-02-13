package com.kdp.golf.user;

import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import javax.sql.DataSource;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class UserDaoTest {

    @Inject DataSource dataSource;
    @Inject UserDao userDao;

    @Test
    @TestTransaction
    void create() throws SQLException {
        try (var connection = dataSource.getConnection()) {
            var user = userDao.create(connection, "Charlie", "session0");
            assertNotNull(user);
            assertEquals(1, userDao.findAll(connection).size());

            userDao.create(connection, "Charlie", "session0");
            assertEquals(2, userDao.findAll(connection).size());
        }
    }

    @Test
    @TestTransaction
    void findAll() throws SQLException {
        try (var connection = dataSource.getConnection()) {
            assertTrue(userDao.findAll(connection).isEmpty());
            userDao.create(connection, "Dee", "s0");
            assertEquals(1, userDao.findAll(connection).size());
        }
    }

    @Test
    @TestTransaction
    void findById() throws SQLException {
        try (var connection = dataSource.getConnection()) {
            var user = userDao.create(connection, "Frank", "session0").orElseThrow();
            assertNotNull(user.id());
            assertEquals("Frank", user.name());
            assertEquals("session0", user.sessionId());
        }
    }

    @Test
    @TestTransaction
    void findBySessionId() throws SQLException {
        try (var connection = dataSource.getConnection()) {
            var user = userDao.create(connection, "Charlie", "session0").orElseThrow();
            var foundUser = userDao.findBySessionId(connection, "session0").orElseThrow();
            assertEquals(user, foundUser);
        }
    }

    @Test
    @TestTransaction
    void updateName() throws SQLException {
        try (var connection = dataSource.getConnection()) {
            var oldName = "Gandalf the Grey";
            var newName = "Gandalf the Green";

            var user = userDao.create(connection, oldName, "s0").orElseThrow();
            assertEquals(oldName, user.name());

            user = user.withName(newName);
            userDao.update(connection, user);

            var foundUser = userDao.findById(connection, user.id()).orElseThrow();
            assertEquals(newName, foundUser.name());
        }
    }

    @Test
    @TestTransaction
    void delete() throws SQLException {
        try (var connection = dataSource.getConnection()) {
            assertTrue(userDao.findAll(connection).isEmpty());

            var user = userDao.create(connection, "Frank", "session9000").orElseThrow();
            assertEquals(1, userDao.findAll(connection).size());

            userDao.delete(connection, user.id());
            assertTrue(userDao.findAll(connection).isEmpty());
        }
    }
}