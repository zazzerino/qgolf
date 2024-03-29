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

        var card = deck.deal().orElseThrow();
        assertNotNull(card);
        assertEquals(DECK_SIZE - 1, deck.cards().size());
    }

    @Test
    void shuffle() {
        var deck0 = Deck.create(1);
        var card0 = deck0.cards().stream().findFirst();

        var deck1 = Deck.create(1);
        var card1 = deck1.cards().stream().findFirst();

        assertEquals(card0, card1);
        deck1.shuffle();
    }

    @Test
    void equals() {
        var deck = Deck.create(1);
        var deck2 = Deck.create(1);

        assertEquals(deck, deck);
        assertEquals(deck, deck2);
    }
}