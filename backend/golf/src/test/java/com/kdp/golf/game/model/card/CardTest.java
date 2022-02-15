package com.kdp.golf.game.model.card;

import com.kdp.golf.game.model.Card;
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
}