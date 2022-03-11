package com.kdp.golf.websocket;

import com.kdp.golf.game.GameController;
import com.kdp.golf.game.dto.GameDto;
import com.kdp.golf.game.model.Game;
import com.kdp.golf.user.UserController;
import com.kdp.golf.user.UserService;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.websocket.*;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@ApplicationScoped
@ServerEndpoint(
        value = "/ws",
        decoders = MessageDecoder.class,
        encoders = ResponseEncoder.class
)
public class WebSocket {

    private final UserService userService;
    private final UserController userController;
    private final GameController gameController;
    private final Map<String, Session> sessions = new ConcurrentHashMap<>();
    private final Logger log = Logger.getLogger(WebSocket.class);

    public WebSocket(UserService userService,
                     UserController userController,
                     GameController gameController) {
        this.userService = userService;
        this.userController = userController;
        this.gameController = gameController;
    }

    @OnOpen
    public void onOpen(Session session) {
        var id = session.getId();
        log.info("websocket connected: " + id);
        sessions.put(id, session);
        userController.connect(session);
    }

    @OnClose
    public void onClose(Session session) {
        var id = session.getId();
        log.info("websocket closed: " + id);
        sessions.remove(id);
        userController.disconnect(id);
    }

    @OnMessage
    public void onMessage(Session session, Message message) {
        log.info("message received: " + message);

        switch (message.type()) {
            case UpdateName -> handleUpdateName(session, (Message.UpdateName) message);
            case CreateGame -> handleCreateGame(session);
            case StartGame -> handleStartGame(session, (Message.StartGame) message);
        }
    }

    @OnError
    public void onError(Session _s, Throwable throwable) {
        log.error("ws error: ", throwable);
    }

    public void sendToSession(Session session, Response response) {
        session.getAsyncRemote()
                .sendObject(response, result -> {
                    if (result.getException() != null) {
                        log.error("error sending to " + session.getId() + ": " + result.getException());
                    }
                });
    }

    public void sendToSessionId(String sessionId, Response response) {
        sendToSession(sessions.get(sessionId), response);
    }

    public void updatePlayers(Game game) {
        for (var player : game.players()) {
            var id = player.id();
            var sessionId = userService.findSessionId(id).orElseThrow();
            var gameDto = GameDto.from(game, id);
            var response = new Response.Game(gameDto);
            sendToSessionId(sessionId, response);
        }
    }

    private void handleUpdateName(Session session, Message.UpdateName message) {
        userController.updateName(session, message.name());
    }

    private void handleCreateGame(Session session) {
        gameController.createGame(session);
    }

    private void handleStartGame(Session session, Message.StartGame message) {
        gameController.startGame(session, message.gameId());
    }

    private void handleUncover(Session session, Message.Uncover message) {
//        gameController.un
    }

//    public void sendToSessionIds(Collection<String> sessionIds, Response response) {
//        sessionIds.forEach(id -> sendToSessionId(id, response));
//    }

//    public void broadcast(Response response) {
//        sessions.values()
//                .forEach(s -> sendToSession(s, response));
//    }
}
