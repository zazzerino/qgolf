package com.kdp.golf.game.model;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class CardTest {

    @Test
    void from() {
        var rank = Card.Rank.from("2").orElseThrow();
        assertEquals(Card.Rank.Two, rank);

        var suit = Card.Suit.from("D").orElseThrow();
        assertEquals(Card.Suit.Diamonds, suit);

        var card = Card.from("JH");
        assertEquals(new Card(Card.Rank.Jack, Card.Suit.Hearts), card);
    }
}