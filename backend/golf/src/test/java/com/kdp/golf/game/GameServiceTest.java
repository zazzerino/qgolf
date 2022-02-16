package com.kdp.golf.game;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
class GameServiceTest {

//    @Inject UserService userService;
//    @Inject GameService gameService;
//
//    @Test
//    @TestTransaction
//    void createGame() {
//        var user = userService.createUser("session0");
//        var game = gameService.createGame(user.id());
//        assertNotNull(game);
//
//        var foundGame = gameService.findGameById(game.id()).orElseThrow();
//        assertNotNull(foundGame);
//        assertEquals(game, foundGame);
//    }
//
//    @Test
//    @TestTransaction
//    void startGame() {
//        var user = userService.createUser("session0");
//        var game = gameService.createGame(user.id());
//        assertEquals(GameState.Init, game.state());
//
////        var startedGame = gameService.startGame(game.id(), user.id());
////        assertNotEquals(game, startedGame);
////        assertEquals(GameState.UncoverTwo, startedGame.state());
//    }
}