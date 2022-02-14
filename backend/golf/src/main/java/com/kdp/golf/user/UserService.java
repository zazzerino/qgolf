package com.kdp.golf.user;

import javax.enterprise.context.ApplicationScoped;
import javax.sql.DataSource;
import javax.transaction.Transactional;
import java.util.Optional;

@ApplicationScoped
public class UserService {

    private final DataSource dataSource;
    private final UserRepository userRepository;

    public UserService(DataSource dataSource, UserRepository UserRepository) {
        this.dataSource = dataSource;
        this.userRepository = UserRepository;
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> findBySessionId(String sessionId) {
        return userRepository.findBySessionId(sessionId);
    }

    public Optional<Long> findUserId(String sessionId) {
        return findBySessionId(sessionId)
                .map(User::id);
    }

    @Transactional
    public Optional<User> createUser(String sessionId) {
        var name = User.DEFAULT_NAME;
        var user = User.of(name, sessionId);
        return userRepository.create(user);
    }

    @Transactional
    public Optional<User> updateName(Long userId, String name) {
        var user = userRepository.findById(userId).orElseThrow();
        var success = userRepository.update(user.withName(name));
        return success ? Optional.of(user) : Optional.empty();
    }

    @Transactional
    public boolean deleteUser(String sessionId) {
        var userId = findUserId(sessionId).orElseThrow();
        return userRepository.delete(userId);
    }
}
