package com.kdp.golf.user.db;

import com.kdp.golf.Repository;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class UserRepository implements Repository<Long, UserEntity> {

    private final EntityManager manager;

    public UserRepository(EntityManager manager) {
        this.manager = manager;
    }

    @Override
    public List<UserEntity> findAll() {
        var query = manager.createQuery(
                "SELECT u FROM User u",
                UserEntity.class);

        return query.getResultList();
    }

    @Override
    public Optional<UserEntity> findById(Long id) {
        var hql = """
                SELECT u FROM User u
                WHERE u.id = :id""";

        var query = manager.createQuery(hql, UserEntity.class);
        query.setParameter("id", id);
        var result = query.getSingleResult();

        return Optional.ofNullable(result);
    }

    public Optional<UserEntity> findBySessionId(String sessionId) {
        var hql = """
                SELECT u FROM User u
                WHERE u.sessionId = :sessionId""";

        var query = manager.createQuery(hql, UserEntity.class);
        query.setParameter("sessionId", sessionId);
        var result = query.getSingleResult();

        return Optional.ofNullable(result);
    }

    @Override
    public UserEntity create(UserEntity user) {
        manager.persist(user);
        return user;
    }

    @Override
    public void update(UserEntity user) {
        manager.persist(user);
    }

    @Override
    public void delete(Long id) {
        var user = findById(id).orElseThrow();
        manager.remove(user);
    }
}
