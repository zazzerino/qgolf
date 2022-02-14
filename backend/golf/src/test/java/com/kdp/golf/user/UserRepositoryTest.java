package com.kdp.golf.user;

import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import javax.sql.DataSource;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class UserRepositoryTest {

    @Inject DataSource dataSource;
    @Inject UserRepository userRepository;

    @Test
    @TestTransaction
    void create() {
        assertTrue(userRepository.findAll().isEmpty());

        var user = userRepository
                .create(User.of("Charlie", "s0"))
                .orElseThrow();

        assertNotNull(user);
        assertTrue(user.id() > 0);
        assertEquals(1, userRepository.findAll().size());
    }

    @Test
    @TestTransaction
    void findById() {
        var user = userRepository
                .create(User.of("Frank", "s0"))
                .orElseThrow();

        assertNotNull(user.id());
        assertEquals("Frank", user.name());
        assertEquals("s0", user.sessionId());
    }

    @Test
    @TestTransaction
    void findBySessionId() {
        var user = userRepository
                .create(User.of("Charlie", "s0"))
                .orElseThrow();

        var foundUser = userRepository.findBySessionId("s0").orElseThrow();
        assertEquals(user, foundUser);
    }

    @Test
    @TestTransaction
    void updateName() {
        var oldName = "Gandalf the Grey";
        var newName = "Gandalf the Green";

        var user = userRepository
                .create(User.of(oldName, "s0"))
                .orElseThrow();

        assertEquals(oldName, user.name());

        var res = userRepository.update(user.withName(newName));
        assertTrue(res);

        var foundUser = userRepository.findById(user.id()).orElseThrow();
        assertEquals(newName, foundUser.name());

    }

    @Test
    @TestTransaction
    void delete() {
        assertTrue(userRepository.findAll().isEmpty());

        var user = userRepository
                .create(User.of("Frank", "session9000"))
                .orElseThrow();

        assertEquals(1, userRepository.findAll().size());

        var res = userRepository.delete(user.id());
        assertTrue(res);

        assertTrue(userRepository.findAll().isEmpty());
    }
}