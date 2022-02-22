package com.kdp.golf.game.db;

import com.kdp.golf.game.model.Card;
import com.kdp.golf.game.model.ImmutableHand;
import com.kdp.golf.game.model.ImmutablePlayer;
import com.kdp.golf.game.model.Player;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import javax.annotation.Nullable;;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public record PlayerRow(Long game,
                        Long user,
                        List<String> handCards,
                        List<Integer> uncoveredCards,
                        @Nullable String heldCard) {

    public static PlayerRow from(Long gameId, Player player) {
        var handCards = player.hand().cards().stream()
                .map(Card::name)
                .toList();

        var uncoveredCards = player.hand().uncovered().stream().toList();

        var heldCard = player.heldCard()
                .map(Card::name)
                .orElse(null);

        return new PlayerRow(gameId, player.id(), handCards, uncoveredCards, heldCard);
    }

    public Player toPlayer(String name) {
        var cards = handCards.stream()
                .map(Card::from)
                .toList();

        var hand = ImmutableHand.of(cards, uncoveredCards);
        var heldCard = Optional.ofNullable(heldCard())
                .map(Card::from);

        return ImmutablePlayer.of(user, name, hand, heldCard);
    }

    public static class Mapper implements RowMapper<PlayerRow> {
        @Override
        public PlayerRow map(ResultSet rs, StatementContext ctx) throws SQLException {
            var game = rs.getLong("game");
            var user = rs.getLong("person");

            var handCards = Arrays.asList(
                    (String[]) rs.getArray("hand_cards").getArray());

            var uncoveredCards = Arrays.asList(
                    (Integer []) rs.getArray("uncovered_cards").getArray());

            var heldCard = rs.getString("held_card");
            return new PlayerRow(game, user, handCards, uncoveredCards, heldCard);
        }
    }
}
