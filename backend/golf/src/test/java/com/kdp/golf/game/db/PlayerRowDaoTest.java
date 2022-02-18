package com.kdp.golf.game.db;

import com.kdp.golf.DatabaseConnection;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class PlayerRowDaoTest {

    DatabaseConnection dbConn;

    public PlayerRowDaoTest(DatabaseConnection dbConn) {
        this.dbConn = dbConn;
    }

    @Test
    @TestTransaction
    void createAndFind() {
        var playerDao = dbConn.jdbi().onDemand(PlayerRowDao.class);

        var handCards = List.of("AC", "4S");
        var uncoveredCards = List.of(2, 5);
        var playerRow = new PlayerRow(
                11L, 42L, handCards, uncoveredCards, null);

        playerDao.create(playerRow);

        var found = playerDao.findById(playerRow.user()).orElseThrow();
        assertEquals(playerRow, found);
    }
}