package com.kdp.golf.game.db;

import com.kdp.golf.lib.DatabaseConnection;
import com.kdp.golf.game.model.Game;
import com.kdp.golf.game.model.Player;
import com.kdp.golf.user.UserService;

import javax.enterprise.context.ApplicationScoped;
import java.util.Optional;

@ApplicationScoped
public class GameRepository {

    private final PlayerRowDao playerRowDao;
    private final GameRowDao gameRowDao;
    private final UserService userService;

    public GameRepository(DatabaseConnection dbConn, UserService userService) {
        var jdbi = dbConn.jdbi();
        this.playerRowDao = jdbi.onDemand(PlayerRowDao.class);
        this.gameRowDao = jdbi.onDemand(GameRowDao.class);
        this.userService = userService;
    }

    public Optional<Game> findById(Long id) {
        var gameRow = gameRowDao.findById(id);
        if (gameRow.isEmpty()) return Optional.empty();

        var players = playerRowDao.findPlayers(id)
                .stream()
                .map(p -> {
                    var name = userService.findName(p.user()).orElseThrow();
                    return p.toPlayer(name);
                })
                .toList();

        var game = gameRow.get().toGame(players);
        return Optional.of(game);
    }

    public Game create(Long userId) {
        var user = userService.findById(userId).orElseThrow();
        var player = Player.from(user);

        var game = Game.create(null, player);
        var gameRow = GameRow.from(game);
        var gameId = gameRowDao.create(gameRow);
        game.setId(gameId);

        var playerRow = PlayerRow.from(gameId, player);
        playerRowDao.create(playerRow);

        return game;
    }

    public void update(Game game) {
        var gameRow = GameRow.from(game);
        gameRowDao.update(gameRow);
    }

    public void delete(Long gameId) {
        gameRowDao.delete(gameId);
    }
}
