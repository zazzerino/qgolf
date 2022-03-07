package com.kdp.golf.user;

import com.kdp.golf.lib.DatabaseConnection;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import java.util.Optional;

@ApplicationScoped
public class UserService {

    private final UserDao userDao;

    public UserService(DatabaseConnection dbConn) {
        userDao = dbConn.jdbi().onDemand(UserDao.class);
    }

    public Optional<User> findById(Long id) {
        return userDao.findById(id);
    }

    public Optional<User> findBySessionId(String sessionId) {
        return userDao.findBySessionId(sessionId);
    }

    public Optional<Long> findUserId(String sessionId) {
        return findBySessionId(sessionId)
                .map(User::id);
    }

    public Optional<String> findName(Long userId) {
        return userDao.findById(userId)
                .map(User::name);
    }

    @Transactional
    public User createUser(String sessionId) {
        var name = User.DEFAULT_NAME;
        var id = userDao.create(name, sessionId);
        return new User(id, name, sessionId);
    }

    @Transactional
    public User updateName(Long userId, String name) {
        var user = userDao.findById(userId).orElseThrow();
        user.setName(name);
        userDao.update(user);
        return user;
    }

    @Transactional
    public void deleteUser(String sessionId) {
        var userId = findUserId(sessionId).orElseThrow();
        userDao.delete(userId);
    }
}
