package com.kdp.golf.game.db;

import com.kdp.golf.game.model.Card;
import com.kdp.golf.game.model.Hand;
import com.kdp.golf.game.model.Player;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import javax.annotation.Nullable;;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import static java.util.stream.Collectors.toCollection;

public record PlayerRow(Long game,
                        Long user,
                        List<String> handCards,
                        List<Integer> uncovered,
                        @Nullable String heldCard) {

    public static PlayerRow from(Long gameId, Player p) {
        var handCards = p.hand().cards().stream()
                .map(Card::name)
                .collect(toCollection(ArrayList::new));

        var uncoveredCards = p.hand().uncovered().stream().toList();

        var heldCard = p.heldCard()
                .map(Card::name)
                .orElse(null);

        return new PlayerRow(gameId, p.id(), handCards, uncoveredCards, heldCard);
    }

    public Player toPlayer(String name) {
        var cards = handCards.stream()
                .map(Card::from)
                .collect(toCollection(ArrayList::new));

        var hand = new Hand(cards, new HashSet<>(uncovered));
        var card = heldCard != null ? Card.from(heldCard) : null;
        return new Player(user, name, hand, card);
    }

    public static class Mapper implements RowMapper<PlayerRow> {

        @Override
        public PlayerRow map(ResultSet rs, StatementContext ctx) throws SQLException {
            var game = rs.getLong("game");
            var user = rs.getLong("person");

            var handCards = Arrays.asList(
                    (String[]) rs.getArray("hand_cards").getArray());

            var uncoveredCards = Arrays.asList(
                    (Integer []) rs.getArray("uncovered").getArray());

            var heldCard = rs.getString("held_card");
            return new PlayerRow(game, user, handCards, uncoveredCards, heldCard);
        }
    }
}
