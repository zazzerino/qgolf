package com.kdp.golf.game.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    @Test
    public void create() {
        var host = Player.create(0L, "Charlie");
        var game = Game.create(0L, host);

        assertEquals(1, game.players().size());
    }

    @Test
    public void addPlayer() {
        var host = Player.create(0L, "Charlie");
        var game = Game.create(0L, host);

        assertEquals(1, game.players().size());

        var player = Player.create(1L, "Dee");
        game.addPlayer(player);

        assertEquals(2, game.players().size());
    }

    @Test
    public void dealStartingHands() {
        var host = Player.create(0L, "Charlie");
        var player2 = Player.create(1L, "Dee");

        var game = Game.create(0L, host);
        game.addPlayer(player2);
        game.dealStartingHands();
    }

    @Test
    public void dealTableCard() {
        var host = Player.create(0L, "Charlie");
        var game = Game.create(0L, host);
        game.dealTableCard();
        assertEquals(1, game.tableCards().size());
    }

    @Test
    public void start() {
        var host = Player.create(0L, "Charlie");
        var game = Game.create(0L, host);
        game.start();
    }

    @Test
    void uncoverCard() {
        var host = Player.create(0L, "Artemis");
        var game = Game.create(0L, host);
        game.start();
        game.uncover(host.id(), 0);
    }    
}