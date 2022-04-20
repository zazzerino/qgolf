package com.kdp.golf.websocket.message;

public record CreateGameMessage(Long userId)
        implements Message {

    @Override
    public Type type() {
        return Type.CreateGame;
    }
}
