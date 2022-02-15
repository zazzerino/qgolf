package com.kdp.golf.game.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    @Test
    void giveCard() {
        var card = Card.from("AS");
        var player = Player.create(0L, "Toots");
        player.giveCard(card);

        assertEquals(1, player.hand().cards().size());
    }

    @Test
    void uncover() {
        var card = Card.from("AS");
        var player = Player.create(0L, "Mariah");
        player.giveCard(card);
        player.uncoverCard(0);

        assertTrue(player.hand()
                .uncoveredCards()
                .contains(0));
    }

    @Test
    void hold() {
        var card = Card.from("KH");
        var player = Player.create(0L, "Toots");
        player.holdCard(card);

        assertEquals(card, player.heldCard().orElseThrow());
    }

    @Test
    void discard() {
        var card = Card.from("KH");
        var player = Player.create(0L, "Mariah");
        player.holdCard(card);

        assertTrue(player.heldCard().isPresent());
        assertEquals(card, player.heldCard().get());

        var discardedCard = player.discard();
        assertTrue(player.heldCard().isEmpty());
        assertNotNull(discardedCard);
    }
}