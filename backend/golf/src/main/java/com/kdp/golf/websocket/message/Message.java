package com.kdp.golf.websocket.message;

public interface Message {

    Type type();
    Long userId();

    enum Type {
        UpdateName,
        CreateGame,
        JoinGame,
        StartGame,
        GameEvent,
        Chat,
    }
}
