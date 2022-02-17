package com.kdp.golf.game.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.kdp.golf.Lib.Pair;
import com.kdp.golf.user.User;
import org.immutables.value.Value;

import java.util.Optional;

@Value.Immutable
@JsonSerialize(as = ImmutablePlayer.class)
public abstract class Player {

    @Value.Parameter
    public abstract Long id();
    @Value.Parameter
    public abstract String name();
    @Value.Parameter
    public abstract Hand hand();
    @Value.Parameter
    public abstract Optional<Card> heldCard();

    public static Player create(Long id, String name) {
        return ImmutablePlayer.of(id, name, Hand.empty(), Optional.empty());
    }

    public static Player from(User user) {
        return Player.create(user.id(), user.name());
    }

    public Player giveCard(Card card) {
        var hand = hand().addCard(card);

        return ImmutablePlayer.copyOf(this)
                .withHand(hand);
    }

    public Player uncoverCard(int handIndex) {
        var hand = hand().uncover(handIndex);

        return ImmutablePlayer.copyOf(this)
                .withHand(hand);
    }

    public Player holdCard(Card card) {
        return ImmutablePlayer.copyOf(this)
                .withHeldCard(Optional.of(card));
    }

    public Pair<Optional<Card>, Player> discardHeldCard() {
        if (heldCard().isEmpty()) {
            return Pair.of(Optional.empty(), this);
        }

        var player = ImmutablePlayer.copyOf(this)
                .withHeldCard(Optional.empty());

        return Pair.of(heldCard(), player);
    }

    public Pair<Optional<Card>, Player> swapCard(int handIndex) {
        if (heldCard().isEmpty()) {
            return Pair.of(Optional.empty(), this);
        }

        var pair = hand().swapCard(heldCard().get(), handIndex);
        var card = pair.a();
        var hand = pair.b();
        var player = ImmutablePlayer.copyOf(this)
                .withHand(hand);

        return Pair.of(card, player);
    }

    public int uncoveredCardCount() {
        return hand().uncoveredCards().size();
    }
}
