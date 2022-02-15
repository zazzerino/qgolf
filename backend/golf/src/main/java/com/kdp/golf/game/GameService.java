package com.kdp.golf.game;

import com.kdp.golf.game.db.GameEntity;
import com.kdp.golf.game.db.GameRepository;
import com.kdp.golf.game.model.Game;
import com.kdp.golf.game.model.Player;
import com.kdp.golf.user.UserService;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import java.util.Objects;
import java.util.Optional;

@ApplicationScoped
public class GameService {

    private final GameRepository gameRepository;
    private final UserService userService;
    private final Logger log = Logger.getLogger(GameService.class);

    public GameService(GameRepository gameRepository, UserService userService) {
        this.gameRepository = gameRepository;
        this.userService = userService;
    }

    public Optional<Game> findGameById(Long gameId) {
        return gameRepository.findById(gameId)
                .map(GameEntity::toGame);
    }

    public Optional<Long> findHostId(Long gameId) {
        return findGameById(gameId)
                .map(Game::hostId);
    }

    @Transactional
    public Game createGame(Long userId) {
        var user = userService.findById(userId).orElseThrow();
        var player = Player.from(user);
        var game = Game.create(null, player);
        var entity = gameRepository.create(GameEntity.from(game));

        return entity.toGame();
    }

    @Transactional
    public Game startGame(Long gameId, Long userId) {
        var entity = gameRepository.findById(gameId).orElseThrow();
        var game = entity.toGame();

        if (!Objects.equals(game.hostId(), userId)) {
            log.error("user " + userId + " attempted to start game " + gameId + " but they are not the host");
            return game;
        }

        game.start();
//        var e = GameEntity.from(game);
        gameRepository.update(GameEntity.from(game));
//        return game;
        return null;
    }
}
