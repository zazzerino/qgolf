package com.kdp.golf.game;

import com.kdp.golf.game.model.GameState;
import com.kdp.golf.user.UserService;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class GameServiceTest {

    @Inject UserService userService;
    @Inject GameService gameService;

    @Test
    @TestTransaction
    void createGame() {
        var user = userService.createUser("session0");
        var game = gameService.createGame(user.id());
        assertNotNull(game);

        var foundGame = gameService.findGameById(game.id()).orElseThrow();
        assertNotNull(foundGame);
        assertEquals(game, foundGame);
    }

    @Test
    @TestTransaction
    void startGame() {
        var user = userService.createUser("session0");
        var game = gameService.createGame(user.id());
        assertEquals(GameState.Init, game.state());

        var startedGame = gameService.startGame(game.id(), user.id());
//        assertNotEquals(game, startedGame);
//        assertEquals(GameState.UncoverTwo, startedGame.state());
    }
}