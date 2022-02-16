package com.kdp.golf.user;

import com.kdp.golf.websocket.Response;
import com.kdp.golf.websocket.WebSocket;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.websocket.Session;

@ApplicationScoped
public class UserController {

    private final WebSocket webSocket;
    private final UserService userService;
    private final Logger log = Logger.getLogger(UserController.class);

    public UserController(WebSocket webSocket, UserService userService) {
        this.webSocket = webSocket;
        this.userService = userService;
    }

    public void connect(Session session) {
        var user = userService.createUser(session.getId());
        log.info("user created: " + user);
        webSocket.sendToSession(session, new Response.User(user));
    }

    public void updateName(Session session, String newName) {
        var id = userService.findUserId(session.getId()).orElseThrow();
        var user = userService.updateName(id, newName);
        webSocket.sendToSession(session, new Response.User(user));
    }

    public void disconnect(String sessionId) {
        var user = userService.findBySessionId(sessionId).orElseThrow();
        userService.deleteUser(sessionId);
        log.info("user deleted: " + user);
    }
}
