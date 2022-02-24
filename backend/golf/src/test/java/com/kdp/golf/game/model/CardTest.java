package com.kdp.golf.game.model;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class CardTest {

    @Test
    void from() {
        var rank = Card.Rank.from("2").orElseThrow();
        assertEquals(Card.Rank.TWO, rank);

        var suit = Card.Suit.from("D").orElseThrow();
        assertEquals(Card.Suit.DIAMONDS, suit);

        var card = Card.from("JH");
        assertEquals(new Card(Card.Rank.JACK, Card.Suit.HEARTS), card);
    }

    @Test
    void equals() {
        var card0 = Card.from("AS");
        var card1 = Card.from("AS");

        assertEquals(card0, card0);
        assertEquals(card1, card1);
    }
}