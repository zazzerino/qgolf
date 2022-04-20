package com.kdp.golf.game.db;

import com.kdp.golf.user.UserService;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class GameRepositoryTest {

    GameRepository gameRepository;
    UserService userService;

    public GameRepositoryTest(GameRepository gameRepository, UserService userService) {
        this.gameRepository = gameRepository;
        this.userService = userService;
    }

    @Test
    @TestTransaction
    void createAndFind() {
        var user = userService.createUser("s0");
        var game = gameRepository.create(user.id());
        System.out.println(game);

        var foundGame = gameRepository.findById(game.id()).orElseThrow();
        assertEquals(game, foundGame);
    }

    @Test
//    @TestTransaction
    void update() {
        var user = userService.createUser("s0");
        var game = gameRepository.create(user.id());
        game.start();
        gameRepository.update(game);
        System.out.println(game);

//        var foundGame = gameRepository.findById(game.id()).orElseThrow();
//        assertEquals(game, foundGame);
    }
}