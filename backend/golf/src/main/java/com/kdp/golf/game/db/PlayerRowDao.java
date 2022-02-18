package com.kdp.golf.game.db;

import org.jdbi.v3.sqlobject.config.RegisterRowMapper;
import org.jdbi.v3.sqlobject.customizer.BindMethods;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;
import java.util.Optional;

public interface PlayerRowDao {

    @SqlQuery("SELECT * FROM player WHERE person = ?")
    @RegisterRowMapper(PlayerRow.Mapper.class)
    Optional<PlayerRow> findById(Long id);

    @SqlQuery("""
        SELECT * FROM player
        JOIN person ON player.person = person.id
        WHERE game = ?""")
    @RegisterRowMapper(PlayerRow.Mapper.class)
    List<PlayerRow> findPlayers(Long gameId);

    @SqlUpdate("""
        INSERT INTO player
        (game, person, hand_cards, uncovered_cards, held_card)
        VALUES (:game, :user, :handCards, :uncoveredCards, :heldCard)""")
    void create(@BindMethods PlayerRow playerRow);

    @SqlUpdate("""
        UPDATE player
        SET hand_cards = :handCards, uncovered_cards = :uncoveredCards, held_card = :heldCard
        WHERE game = :game AND person = :user""")
    void update(@BindMethods PlayerRow playerRow);

    @SqlUpdate("DELETE FROM player WHERE person = ?")
    void delete(Long playerId);

    @SqlUpdate("DELETE FROM player WHERE game = ?")
    void deletePlayers(Long gameId);
}
