package com.kdp.golf.game.model;

import com.google.errorprone.annotations.Var;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class GameTest {

    @Test
    public void create() {
        var host = Player.create(0L, "Charlie");
        var game = Game.create(0L, host);
        System.out.println(game);

        assertEquals(1, game.players().size());
    }

    @Test
    public void addPlayer() {
        var host = Player.create(0L, "Charlie");
        @Var var game = Game.create(0L, host);

        assertEquals(1, game.players().size());

        var player = Player.create(1L, "Dee");
        game = game.addPlayer(player);
        System.out.println(game);

        assertEquals(2, game.players().size());
    }

    @Test
    public void dealStartingHands() {
        var host = Player.create(0L, "Charlie");
        var player2 = Player.create(1L, "Dee");

        @Var var game = Game.create(0L, host);
        game = game.addPlayer(player2);
        game = game.dealStartingHands();

        System.out.println(game);
    }

//    @Test
//    public void dealTableCard() {
//        var host = Player.create(0L, "Charlie");
//        @Var var game = Game.create(0L, host);
//        game = game.dealTableCard();
//        assertEquals(1, game.tableCards().size());
//    }
//
//    @Test
//    public void start() {
//        var host = Player.create(0L, "Charlie");
//        @Var var game = Game.create(0L, host);
//        game = game.start();
////        System.out.println(game);
//    }
//
//    @Test
//    void uncoverCard() {
//        var host = Player.create(0L, "Artemis");
//        @Var var game = Game.create(0L, host);
//        game = game.start();
//        game = game.uncover(host.id(), 0);
////        System.out.println(game);
//    }
}