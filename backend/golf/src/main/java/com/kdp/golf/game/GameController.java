package com.kdp.golf.game;

import com.kdp.golf.game.dto.GameDto;
import com.kdp.golf.game.model.GameEvent;
import com.kdp.golf.user.UserService;
import com.kdp.golf.websocket.response.GameResponse;
import com.kdp.golf.websocket.WebSocket;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.websocket.Session;

@ApplicationScoped
public class GameController {

    private final WebSocket webSocket;
    private final GameService gameService;
    private final UserService userService;
    private final Logger log = Logger.getLogger(GameController.class);

    public GameController(WebSocket webSocket, GameService gameService, UserService userService) {
        this.webSocket = webSocket;
        this.gameService = gameService;
        this.userService = userService;
    }

    public void createGame(Session session) {
        var userId = userService.findUserId(session.getId()).orElseThrow();
        var game = gameService.createGame(userId);
        log.info("game created: " + game);
        var gameDto = GameDto.from(game, userId);
        var response = new GameResponse(gameDto);
        webSocket.sendToSession(session, response);
    }

    public void startGame(Session session, Long gameId) {
        var userId = userService.findUserId(session.getId()).orElseThrow();
        var game = gameService.startGame(gameId, userId);
        log.info("game started: " + game);
        webSocket.updatePlayers(game);
    }

    public void handleGameEvent(Session _session, GameEvent event) {
        log.info("handling event: " + event);
        var game = gameService.handleGameEvent(event);
        log.info("event handled: " + game);
        webSocket.updatePlayers(game);
    }
}
