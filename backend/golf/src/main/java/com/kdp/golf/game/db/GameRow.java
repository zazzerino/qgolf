package com.kdp.golf.game.db;

import com.kdp.golf.game.model.*;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toCollection;

public record GameRow(Long id,
                      Long host,
                      String state,
                      int turn,
                      boolean finalTurn,
                      List<String> deck,
                      List<String> tableCards,
                      List<Long> players) {
    
    public static GameRow from(Game g) {
        var deck = g.deck().cards()
                .stream()
                .map(Card::name)
                .toList();

        var tableCards = g.tableCards()
                .stream()
                .map(Card::name)
                .toList();

        var players = g.players()
                .stream()
                .map(Player::id)
                .toList();

        return new GameRow(
                g.id(),
                g.hostId(),
                g.state().toString(),
                g.turn(),
                g.isFinalTurn(),
                deck,
                tableCards,
                players);
    }

    public Game toGame(List<Player> players) {
        var state = Game.State.valueOf(state());
        var deckCards = deck.stream()
                .map(Card::from)
                .collect(toCollection(ArrayDeque::new));

        var deck = new Deck(deckCards);
        var tableCards = tableCards().stream()
                .map(Card::from)
                .collect(toCollection(ArrayDeque::new));

        assert players().equals(players.stream().map(Player::id).toList());
        var playerMap = players.stream()
                .collect(Collectors.toMap(
                        Player::id,
                        Function.identity(),
                        (_prev, next) -> next,
                        LinkedHashMap::new));

        return new Game(id, host, state, turn, finalTurn, deck, tableCards, playerMap);
    }

    public static class Mapper implements RowMapper<GameRow> {

        @Override
        public GameRow map(ResultSet rs, StatementContext ctx) throws SQLException {
            var id = rs.getLong("id");
            var host = rs.getLong("host");
            var state = rs.getString("state");
            var turn = rs.getInt("turn");
            var isFinalTurn = rs.getBoolean("final_turn");

            var deck = Arrays.asList(
                    (String []) rs.getArray("deck").getArray());

            var tableCards = Arrays.asList(
                    (String []) rs.getArray("table_cards").getArray());

            var players = Arrays.asList(
                    (Long []) rs.getArray("players").getArray());

            return new GameRow(id, host, state, turn, isFinalTurn, deck, tableCards, players);
        }
    }
}
