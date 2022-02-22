package com.kdp.golf.websocket;

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
    private final Logger log = Logger.getLogger(WebSocket.class);

    private final UserController userController;

    public WebSocket(UserController userController) {
        this.userController = userController;
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
