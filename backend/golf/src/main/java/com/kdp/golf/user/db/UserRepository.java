package com.kdp.golf.user.db;

import com.kdp.golf.Repository;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class UserRepository implements Repository<Long, UserEntity> {

    private final EntityManager entityManager;

    public UserRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<UserEntity> findAll() {
        var query = entityManager.createQuery(
                "SELECT u FROM User u",
                UserEntity.class);

        return query.getResultList();
    }

    @Override
    public Optional<UserEntity> findById(Long id) {
        var hql = """
                SELECT u FROM User u
                WHERE u.id = :id""";

        var query = entityManager.createQuery(hql, UserEntity.class);
        query.setParameter("id", id);
        var entity = query.getSingleResult();

        return Optional.ofNullable(entity);
    }

    public Optional<UserEntity> findBySessionId(String sessionId) {
        var hql = """
                SELECT u FROM User u
                WHERE u.sessionId = :sessionId""";

        var query = entityManager.createQuery(hql, UserEntity.class);
        query.setParameter("sessionId", sessionId);
        var result = query.getSingleResult();

        return Optional.ofNullable(result);
    }

    @Override
    public UserEntity create(UserEntity user) {
        entityManager.persist(user);
        return user;
    }

    @Override
    public void update(UserEntity user) {
        entityManager.persist(user);
    }

    @Override
    public void delete(Long id) {
        var entity = findById(id).orElseThrow();
        entityManager.remove(entity);
    }
}
