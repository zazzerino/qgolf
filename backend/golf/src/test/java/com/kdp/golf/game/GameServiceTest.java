package com.kdp.golf.game;

import com.kdp.golf.game.model.Game;
import com.kdp.golf.user.UserService;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class GameServiceTest {

    private final UserService userService;
    private final GameService gameService;

    public GameServiceTest(UserService userService, GameService gameService) {
        this.userService = userService;
        this.gameService = gameService;
    }

    @Test
    @TestTransaction
    void createAndFind() {
        var user = userService.createUser("session0");
        var game = gameService.createGame(user.id());
        assertNotNull(game);

        var found = gameService.findGameById(game.id()).orElseThrow();
        assertEquals(game, found);
    }


    @Test
    @TestTransaction
    void startGame() {
        var user = userService.createUser("session0");
        var game = gameService.createGame(user.id());
        assertEquals(Game.State.INIT, game.state());

        var startedGame = gameService.startGame(game.id(), user.id());
        assertNotEquals(game, startedGame);
        assertEquals(Game.State.UNCOVER_TWO, startedGame.state());
//        System.out.println(startedGame);

        var foundGame = gameService.findGameById(game.id()).orElseThrow();
        System.out.println(foundGame);
        assertEquals(startedGame, foundGame);
    }
}