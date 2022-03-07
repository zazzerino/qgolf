package com.kdp.golf.websocket;

import com.kdp.golf.game.GameController;
import com.kdp.golf.user.UserController;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.websocket.*;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@ApplicationScoped
@ServerEndpoint(
        value = "/ws",
        decoders = MessageDecoder.class,
        encoders = ResponseEncoder.class
)
public class WebSocket {

    private final Map<String, Session> sessions = new ConcurrentHashMap<>();
    private final UserController userController;
    private final GameController gameController;
    private final Logger log = Logger.getLogger(WebSocket.class);

    public WebSocket(UserController userController, GameController gameController) {
        this.userController = userController;
        this.gameController = gameController;
    }

    @OnOpen
    public void onOpen(Session session) {
        var sessionId = session.getId();
        log.info("websocket connected: " + sessionId);
        sessions.put(sessionId, session);
        userController.connect(session);
    }

    @OnClose
    public void onClose(Session session) {
        var sessionId = session.getId();
        log.info("websocket closed: " + sessionId);
        sessions.remove(sessionId);
        userController.disconnect(sessionId);
    }

    @OnMessage
    public void onMessage(Session session, Message message) {
        log.info("message received: " + message);

        switch (message.type()) {
            case UpdateName:
                handleUpdateName(session, message);
                break;
            case CreateGame:
                handleCreateGame(session);
                break;
//            case JoinGame:
//                break;
//            case StartGame:
//                break;
//            case GameEvent:
//                break;
//            case Chat:
//                break;
        }
    }

    private void handleUpdateName(Session session, Message message) {
        var m = (Message.UpdateName) message;
        userController.updateName(session, m.name());
    }

    private void handleCreateGame(Session session) {
        gameController.createGame(session);
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
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

    public void sendToSessionIds(Collection<String> sessionIds, Response response) {
        sessionIds.forEach(
                id -> sendToSessionId(id, response));
    }

    public void broadcast(Response response) {
        sessions.values()
                .forEach(s -> sendToSession(s, response));
    }
}
