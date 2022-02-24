package com.kdp.golf.game.db;

import org.jdbi.v3.sqlobject.config.RegisterRowMapper;
import org.jdbi.v3.sqlobject.customizer.BindMethods;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.Optional;

public interface GameRowDao {

    @SqlQuery("SELECT * FROM game WHERE id = ?")
    @RegisterRowMapper(GameRow.Mapper.class)
    Optional<GameRow> findById(Long id);

    @SqlUpdate("""
        INSERT INTO game
        (host, state, turn, final_turn, deck, table_cards, players)
        VALUES
        (:host, :state, :turn, :finalTurn, :deck, :tableCards, :players)""")
    @GetGeneratedKeys("id")
    Long create(@BindMethods GameRow gameRow);

    @SqlUpdate("""
        UPDATE game
        SET
        host = :host, state = :state, turn = :turn, final_turn = :finalTurn,
        deck = :deck, table_cards = :tableCards, players = :players
        WHERE id = :id""")
    void update(@BindMethods GameRow gameRow);

    @SqlUpdate("DELETE FROM game WHERE id = ?")
    void delete(Long id);
}
