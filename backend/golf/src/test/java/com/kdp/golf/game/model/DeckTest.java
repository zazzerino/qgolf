package com.kdp.golf.game.model;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class DeckTest {

    final int DECK_SIZE = 52;

    @Test
    void create() {
        var singleDeck = Deck.create(1);
        assertEquals(DECK_SIZE, singleDeck.cards().size());

        var doubleDeck = Deck.create(2);
        assertEquals(DECK_SIZE * 2, doubleDeck.cards().size());
    }

    @Test
    void deal() {
        var deck = Deck.create(1);
        assertEquals(DECK_SIZE, deck.cards().size());

        var pair = deck.deal();
        var card = pair.a().orElseThrow();
        var newDeck = pair.b();

        assertNotNull(card);
        assertEquals(deck.cards().size() - 1, newDeck.cards().size());
    }

    @Test
    void shuffle() {
        var deck0 = Deck.create(1);
        var card0 = deck0.cards().get(0);

        var deck1 = Deck.create(1);
        var card1 = deck1.cards().get(0);

        assertEquals(card0, card1);
        deck1.shuffle();
    }
}