package com.kdp.golf.game.db;

import com.kdp.golf.game.model.*;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public record GameRow(Long id,
                      Long host,
                      String state,
                      int turn,
                      List<String> deck,
                      List<String> tableCards,
                      List<Long> playerOrder,
                      boolean finalTurn) {
    
    public static GameRow create(Player host) {
        var hostId = host.id();
        var deck = Deck.create(Game.DECK_COUNT)
                .cards()
                .stream()
                .map(Card::name)
                .toList();
        
        return new GameRow(
                null,
                hostId,
                Game.State.Init.toString(),
                0,
                deck,
                List.of(),
                List.of(hostId),
                false);
    }

    public static GameRow from(Game game) {
        var state = game.state().toString();

        var deck = game.deck().cards()
                .stream()
                .map(Card::name)
                .toList();

        var tableCards = game.tableCards()
                .stream()
                .map(Card::name)
                .toList();

//        return new GameRow(
//                game.id(),
//                game.hostId(),
//                state,
//                game.turn(),
//                deck,
//                tableCards,
//                game.playerOrder(),
//                game.isFinalTurn());
        return null;
    }

    public Game toGame(List<Player> players) {
        var deckCards = deck.stream()
                .map(Card::from)
                .toList();

        var deck = new Deck(deckCards);

        var tableCards = tableCards().stream()
                .map(Card::from)
                .toList();

        var playerMap = players.stream()
                .collect(Collectors.toMap(
                        Player::id,
                        Function.identity()));

//        return ImmutableGame.builder()
//                .id(id)
//                .hostId(host())
//                .state(Game.State.valueOf(state()))
//                .turn(turn)
//                .deck(deck)
//                .tableCards(tableCards)
//                .players(playerMap)
//                .playerOrder(playerOrder)
//                .isFinalTurn(finalTurn)
//                .build();
        return null;
    }
    
    public GameRow withId(Long id) {
        return new GameRow(
                id, host, state, turn, deck, tableCards, playerOrder, finalTurn);
    }

    public static class Mapper implements RowMapper<GameRow> {
        @Override
        public GameRow map(ResultSet rs, StatementContext ctx) throws SQLException {
            var id = rs.getLong("id");
            var host = rs.getLong("host");
            var state = rs.getString("state");
            var turn = rs.getInt("turn");

            var deck = Arrays.asList(
                    (String []) rs.getArray("deck").getArray());

            var tableCards = Arrays.asList(
                    (String []) rs.getArray("table_cards").getArray());

            var playerOrder = Arrays.asList(
                    (Long []) rs.getArray("player_order").getArray());

            var isFinalTurn = rs.getBoolean("final_turn");
            return new GameRow(id, host, state, turn, deck, tableCards, playerOrder, isFinalTurn);
        }
    }
}
