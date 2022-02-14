package com.kdp.golf.user;

import com.kdp.golf.user.db.UserEntity;
import com.kdp.golf.user.db.UserRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import java.util.Optional;

@ApplicationScoped
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id)
                .map(UserEntity::toUser);
    }

    public Optional<User> findBySessionId(String sessionId) {
        return userRepository.findBySessionId(sessionId)
                .map(UserEntity::toUser);
    }

    public Optional<Long> findUserId(String sessionId) {
        return findBySessionId(sessionId)
                .map(User::id);
    }

    @Transactional
    public User createUser(String sessionId) {
        var entity = new UserEntity();
        entity.name = User.DEFAULT_NAME;
        entity.sessionId = sessionId;
        entity = userRepository.create(entity);

        return entity.toUser();
    }

    @Transactional
    public User updateName(Long userId, String name) {
        var entity = userRepository.findById(userId).orElseThrow();
        entity.name = name;
        userRepository.update(entity);

        return entity.toUser();
    }

    @Transactional
    public void deleteUser(String sessionId) {
        var userId = findUserId(sessionId).orElseThrow();
        userRepository.delete(userId);
    }
}
