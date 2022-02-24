package com.kdp.golf.game.model;

import io.quarkus.test.junit.QuarkusTest;
import org.jboss.logging.Logger;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class GameTest {

   Logger log = Logger.getLogger(GameTest.class);

    @Test
    public void create() {
        var host = Player.create(0L, "Charlie");
        var game = Game.create(0L, host);

        assertEquals(1, game.players().size());
    }

    @Test
    public void equals() {
        var host = Player.create(0L, "Charlie");
        var game = Game.create(0L, host);

        log.info(game);
        assertEquals(game, game);

        var game2 = Game.create(0L, host);
        assertEquals(game, game2);
    }

    @Test
    public void addPlayer() {
        var host = Player.create(0L, "Charlie");
        var game = Game.create(0L, host);

        assertEquals(1, game.players().size());

        var player = Player.create(1L, "Dee");
        game.addPlayer(player);
        System.out.println(game);

        assertEquals(2, game.players().size());
    }

    @Test
    public void dealStartingHands() {
        var host = Player.create(0L, "Charlie");
        var player2 = Player.create(1L, "Dee");

        var game = Game.create(0L, host);
        game.addPlayer(player2);
        game.dealStartingHands();

        System.out.println(game);
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
        System.out.println(game);
    }

    @Test
    void uncoverCard() {
        var host = Player.create(0L, "Artemis");
        var game = Game.create(0L, host);
        game.start();
        game.uncover(host, 0);
        System.out.println(game);
    }
}