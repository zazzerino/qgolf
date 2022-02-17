package com.kdp.golf.game.model;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@QuarkusTest
class HandTest {

    @Test
    void create() {
        var hand = Hand.empty();
        assertEquals(0, hand.cards().size());
        assertEquals(0, hand.uncoveredCards().size());
    }

    @Test
    void uncover() {
        var hand = Hand.empty();
        hand = hand.uncover(2);

        assertTrue(hand.uncoveredCards()
                .stream()
                .allMatch(i -> i.equals(2)));
    }

    @Test
    void addCard() {
        var card = Card.from("AS");
        var hand = Hand.empty();
        hand = hand.addCard(card);

        assertEquals(card, hand.cards().get(0));
    }

    @Test
    void swapCard() {
        var cards = Stream.of("2C", "3C", "4C", "5C", "6C", "7C")
                .map(Card::from)
                .toList();

        var hand = ImmutableHand.of(cards, Set.of());
        var newCard = Card.from("8C");
        var pair = hand.swapCard(newCard, 0);
        var swappedCard = pair.a().orElseThrow();

        assertEquals(Card.from("2C"), swappedCard);
        assertEquals(6, hand.cards().size());
    }
}