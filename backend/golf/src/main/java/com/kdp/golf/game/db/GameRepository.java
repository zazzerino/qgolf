package com.kdp.golf.game.db;

import com.google.errorprone.annotations.Var;
import com.kdp.golf.DatabaseConnection;
import com.kdp.golf.game.model.Game;
import com.kdp.golf.game.model.Player;
import com.kdp.golf.user.UserService;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class GameRepository {

    private final PlayerRowDao playerRowDao;
    private final GameRowDao gameRowDao;
    private final UserService userService;

    public GameRepository(DatabaseConnection dbConn, UserService userService) {
        var jdbi = dbConn.jdbi();
        playerRowDao = jdbi.onDemand(PlayerRowDao.class);
        gameRowDao = jdbi.onDemand(GameRowDao.class);

        this.userService = userService;
    }

    public Optional<Game> findById(Long id) {
        var gameRow = gameRowDao.findById(id);

        if (gameRow.isEmpty()) {
            return Optional.empty();
        }

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
        
        @Var var gameRow = GameRow.create(player);
        var id = gameRowDao.create(gameRow);
        gameRow = gameRow.withId(id);
        
        var playerRow = PlayerRow.from(id, player);
        playerRowDao.create(playerRow);
        
        return gameRow.toGame(List.of(player));
    }

    public void update(Game game) {
        var gameRow = GameRow.from(game);
        gameRowDao.update(gameRow);
    }

    public void delete(Long gameId) {
        gameRowDao.delete(gameId);
    }
}
