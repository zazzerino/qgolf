package com.kdp.golf.websocket.message;

public record JoinGameMessage(Long userId,
                              Long gameId)
        implements Message {

    @Override
    public Type type() {
        return Type.JoinGame;
    }
}
