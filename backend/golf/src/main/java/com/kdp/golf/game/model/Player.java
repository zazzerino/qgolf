package com.kdp.golf.game.model;

import com.kdp.golf.lib.Pair;
import com.kdp.golf.user.User;

import javax.annotation.Nullable;
import java.util.Optional;

public record Player(Long id,
                     String name,
                     Hand hand,
                     Optional<Card> heldCard) {

    public Player withHand(Hand hand) {
        return new Player(id, name, hand, heldCard);
    }

    public Player withHeldCard(@Nullable Card heldCard) {
        return new Player(id, name, hand, Optional.ofNullable(heldCard));
    }

    public static Player create(Long id, String name) {
        return new Player(id, name, Hand.empty(), Optional.empty());
    }

    public static Player from(User user) {
        return Player.create(user.id(), user.name());
    }

    public Player giveCard(Card card) {
        var hand = hand().addCard(card);
        return withHand(hand);
    }

    public Player uncoverCard(int handIndex) {
        var hand = hand().uncover(handIndex);
        return withHand(hand);
    }

    public Player holdCard(Card card) {
        return withHeldCard(card);
    }

    public Pair<Optional<Card>, Player> discardHeldCard() {
        if (heldCard().isEmpty()) {
            return Pair.of(Optional.empty(), this);
        }

        var player = withHeldCard(null);
        return Pair.of(heldCard(), player);
    }

    public Pair<Optional<Card>, Player> swapCard(int handIndex) {
        if (heldCard().isEmpty()) {
            return Pair.of(Optional.empty(), this);
        }

        var heldCard = heldCard().get();
        var pair = hand().swapCard(heldCard, handIndex);
        var card = pair.a();
        var hand = pair.b();
        var player = withHand(hand);

        return Pair.of(card, player);
    }

    public int uncoveredCardCount() {
        return hand().uncovered().size();
    }
}
