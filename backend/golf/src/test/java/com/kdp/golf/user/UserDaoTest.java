package com.kdp.golf.user;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
class UserDaoTest {

//    @Inject DatabaseConnection dbConn;
//
//    @Test
//    @TestTransaction
//    void createAndFind() {
//        dbConn.jdbi().useHandle(handle -> {
//            var userDao = handle.attach(UserDao.class);
//            assertTrue(userDao.findAll().isEmpty());
//
//            var id = userDao.create("alice", "s0");
//            var user = userDao.findById(id).orElseThrow();
//            assertNotNull(user);
//            assertEquals("alice", user.name());
//            assertEquals("s0", user.sessionId());
//        });
//    }

//    @Inject UserRepository userRepository;
//
//    @Test
//    @TestTransaction
//    void create() {
//        var user = userRepository.create(
//                new UserEntity(null, "Frank", "s0"));
//
//        assertNotNull(user.id);
//        assertEquals("Frank", user.name);
//        assertEquals("s0", user.sessionId);
//    }
//
//    @Test
//    @TestTransaction
//    void findAll() {
//        assertTrue(userRepository.findAll().isEmpty());
//
//        var user = userRepository.create(
//                new UserEntity(null, "alice", "s0"));
//
//        assertNotNull(user);
//        assertTrue(user.id > 0);
//        assertEquals(1, userRepository.findAll().size());
//    }
//
//    @Test
//    @TestTransaction
//    void findBySessionId() {
//        var user = userRepository.create(
//                new UserEntity(null, "Charlie", "s0"));
//
//        var foundUser = userRepository.findBySessionId("s0").orElseThrow();
//        assertEquals(user, foundUser);
//    }
//
//    @Test
//    @TestTransaction
//    void updateName() {
//        var oldName = "Gandalf the Grey";
//        var newName = "Gandalf the Green";
//
//        var user = userRepository.create(
//                new UserEntity(null, oldName, "s0"));
//
//        assertEquals(oldName, user.name);
//
//        user.name = newName;
//        userRepository.update(user);
//
//        var foundUser = userRepository.findById(user.id).orElseThrow();
//        assertEquals(newName, foundUser.name);
//
//    }
//
//    @Test
//    @TestTransaction
//    void delete() {
//        assertTrue(userRepository.findAll().isEmpty());
//
//        var user = userRepository.create(
//                new UserEntity(null, "Frank", "s0"));
//
//        assertEquals(1, userRepository.findAll().size());
//
//        userRepository.delete(user.id);
//        assertTrue(userRepository.findAll().isEmpty());
//    }
}