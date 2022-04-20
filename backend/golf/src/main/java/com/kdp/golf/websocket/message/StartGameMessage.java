package com.kdp.golf.websocket.message;

public record StartGameMessage(Long userId,
                               Long gameId)
        implements Message {

    @Override
    public Type type() {
        return Type.StartGame;
    }
}
