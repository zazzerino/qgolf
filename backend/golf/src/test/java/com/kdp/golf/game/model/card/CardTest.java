package com.kdp.golf.game.model.card;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class CardTest {

    @Test
    void from() {
        var rank = Rank.from("2").orElseThrow();
        assertEquals(Rank.TWO, rank);

        var suit = Suit.from("D").orElseThrow();
        assertEquals(Suit.DIAMONDS, suit);

        var card = Card.from("JH");
        assertEquals(new Card(Rank.JACK, Suit.HEARTS), card);
    }
}