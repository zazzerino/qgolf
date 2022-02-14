package com.kdp.golf.user;

import com.kdp.golf.user.db.UserEntity;
import com.kdp.golf.user.db.UserRepository;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class UserRepositoryTest {

    @Inject
    UserRepository userRepository;

    @Test
    @TestTransaction
    void create() {
        var user = new UserEntity();
        user.name = "Frank";
        user.sessionId = "s0";

        user = userRepository.create(user);

        assertNotNull(user.id);
        assertEquals("Frank", user.name);
        assertEquals("s0", user.sessionId);
    }

    @Test
    @TestTransaction
    void findAll() {
        assertTrue(userRepository.findAll().isEmpty());

        var user = new UserEntity();
        user.name = "alice";
        user.sessionId = "s0";

        user = userRepository.create(user);

        assertNotNull(user);
        assertTrue(user.id > 0);
        assertEquals(1, userRepository.findAll().size());
    }

    @Test
    @TestTransaction
    void findBySessionId() {
        var user = new UserEntity();
        user.name = "Charlie";
        user.sessionId = "s0";

        user = userRepository.create(user);

        var foundUser = userRepository.findBySessionId("s0").orElseThrow();
        assertEquals(user, foundUser);
    }

    @Test
    @TestTransaction
    void updateName() {
        var oldName = "Gandalf the Grey";
        var newName = "Gandalf the Green";

        var user = new UserEntity();
        user.name = oldName;
        user.sessionId = "s0";

        assertEquals(oldName, user.name);

        user.name = newName;
        userRepository.update(user);

        var foundUser = userRepository.findById(user.id).orElseThrow();
        assertEquals(newName, foundUser.name);

    }

    @Test
    @TestTransaction
    void delete() {
        assertTrue(userRepository.findAll().isEmpty());

        var user = new UserEntity();
        user.name = "Frank";
        user.sessionId = "s0";

        user = userRepository.create(user);

        assertEquals(1, userRepository.findAll().size());

        userRepository.delete(user.id);
        assertTrue(userRepository.findAll().isEmpty());
    }
}