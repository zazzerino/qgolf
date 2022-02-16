package com.kdp.golf.user;

import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class UserServiceTest {

    @Inject
    UserService userService;

    @Test
    @TestTransaction
    void createAndFind() {
        var user = userService.createUser("session0");
        assertNotNull(user.id());
        assertEquals(User.DEFAULT_NAME, user.name());
        assertEquals("session0", user.sessionId());

        var foundById = userService.findById(user.id()).orElseThrow();
        assertEquals(user, foundById);

        var foundBySession = userService.findBySessionId(user.sessionId()).orElseThrow();
        assertEquals(user, foundBySession);
    }

    @Test
    @TestTransaction
    void updateName() {
        var user = userService.createUser("session0");
        var user2 = userService.updateName(user.id(), "Brian Goetz");

        var found = userService.findById(user.id()).orElseThrow();
        assertEquals("Brian Goetz", found.name());
        assertEquals(user2, found);
    }
}