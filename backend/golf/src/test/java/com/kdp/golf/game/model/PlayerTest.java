package com.kdp.golf.game.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    @Test
    void giveCard() {
        var card = Card.from("AS");
        var player = Player.create(0L, "Toots");
        player = player.giveCard(card);

        assertEquals(1, player.hand().cards().size());
    }

    @Test
    void uncover() {
        var card = Card.from("AS");
        var player = Player.create(0L, "Mariah");
        player = player.giveCard(card);
        player = player.uncoverCard(0);

        assertTrue(player.hand()
                .uncovered()
                .contains(0));
    }

    @Test
    void hold() {
        var card = Card.from("KH");
        var player = Player.create(0L, "Toots");
        player = player.holdCard(card);

        assertEquals(card, player.heldCard().orElseThrow());
    }

    @Test
    void discard() {
        var card = Card.from("KH");
        var player = Player.create(0L, "Mariah");
        player = player.holdCard(card);

        assertTrue(player.heldCard().isPresent());
        assertEquals(card, player.heldCard().get());

        var pair = player.discardHeldCard();
        var discardedCard = pair.a().orElseThrow();
        player = pair.b();

        assertTrue(player.heldCard().isEmpty());
        assertNotNull(discardedCard);
    }
}