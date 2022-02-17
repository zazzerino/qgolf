package com.kdp.golf.game;

import com.google.errorprone.annotations.Var;
import com.kdp.golf.game.db.GameRepository;
import com.kdp.golf.game.model.Game;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import java.util.Objects;
import java.util.Optional;

@ApplicationScoped
public class GameService {

    private final GameRepository gameRepository;
    private final Logger log = Logger.getLogger(GameService.class);

    public GameService(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    public Optional<Game> findGameById(Long gameId) {
        return gameRepository.findById(gameId);
    }

    public Optional<Long> findHostId(Long gameId) {
        return findGameById(gameId)
                .map(Game::hostId);
    }

    @Transactional
    public Game createGame(Long userId) {
        var game = gameRepository.create(userId);
        log.info("created game " + game.id());
        return game;
    }

    @Transactional
    public Game startGame(Long gameId, Long userId) {
        @Var var game = gameRepository.findById(gameId).orElseThrow();

        if (!Objects.equals(game.hostId(), userId)) {
            throw new IllegalStateException(
                    "user " + userId + " attempted to start game " + gameId + " but they are not the host");
        }

        game = game.start();
        gameRepository.update(game);
        return game;
    }
}
