package com.kdp.golf.game.db;

import com.kdp.golf.Repository;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class GameRepository implements Repository<Long, GameEntity> {

    private final EntityManager entityManager;

    public GameRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<GameEntity> findAll() {
        var query = entityManager.createQuery(
                "SELECT g FROM Game g",
                GameEntity.class);

        return query.getResultList();
    }

    @Override
    public Optional<GameEntity> findById(Long id) {
        var hql = "SELECT g FROM Game g WHERE g.id = :id";
        var query = entityManager.createQuery(hql, GameEntity.class);
        query.setParameter("id", id);
        var entity = query.getSingleResult();

        return Optional.ofNullable(entity);
    }

    @Override
    public GameEntity create(GameEntity entity) {
        entityManager.persist(entity);
        return entity;
    }

    @Override
    public void update(GameEntity entity) {
//        entityManager.persist(entity);
        entityManager.merge(entity);
    }

    @Override
    public void delete(Long id) {
        var entity = findById(id).orElseThrow();
        entityManager.remove(entity);
    }
}
