//package com.kdp.golf.game.db;
//
//import com.kdp.golf.game.model.Game;
//import com.kdp.golf.game.model.Player;
//import io.quarkus.test.TestTransaction;
//import io.quarkus.test.junit.QuarkusTest;
//import org.junit.jupiter.api.Test;
//
//import javax.inject.Inject;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@QuarkusTest
//class GameRepositoryTest {
//
////    @Inject GameRepository gameRepo;
////
////    @Test
////    @TestTransaction
////    void createAndFind() {
////        assertTrue(gameRepo.findAll().isEmpty());
////
////        var player = Player.create(11L, "Toby");
////        var game = Game.create(null, player);
////
////        var entity = gameRepo.create(
////                GameEntity.from(game));
////
////        var id = entity.id;
////
////        assertNotNull(entity);
////        assertNotNull(id);
////        assertNotNull(entity.players);
////        assertEquals(1, gameRepo.findAll().size());
////
////        var found = gameRepo.findById(id).orElseThrow();
////        assertNotNull(found);
////        assertEquals(entity, found);
////
////        var foundGame = found.toGame();
////        assertEquals(entity.toGame(), foundGame);
////    }
//}