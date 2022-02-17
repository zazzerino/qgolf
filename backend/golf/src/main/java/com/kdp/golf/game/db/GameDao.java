package com.kdp.golf.game.db;

import org.jdbi.v3.sqlobject.config.RegisterRowMapper;
import org.jdbi.v3.sqlobject.customizer.BindMethods;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.Optional;

public interface GameDao {

    @SqlQuery("SELECT * FROM game WHERE id = ?")
    @RegisterRowMapper(GameRow.Mapper.class)
    Optional<GameRow> findById(Long id);

    @SqlUpdate("""
        INSERT INTO game
        (host, state, turn, deck, table_cards, player_order, final_turn)
        VALUES (:host, :state, :turn, :deck, :tableCards, :playerOrder, :finalTurn)""")
    @GetGeneratedKeys("id")
    Long create(@BindMethods GameRow gameRow);

    @SqlUpdate("""
        UPDATE game
        SET
        host = :host, state = :state, turn = :turn, deck = :deck, table_cards = :tableCards,
        player_order = :playerOrder, final_turn = :finalTurn
        WHERE id = :id""")
    void update(@BindMethods GameRow gameRow);

    @SqlUpdate("DELETE FROM game WHERE id = ?")
    void delete(Long id);
}
