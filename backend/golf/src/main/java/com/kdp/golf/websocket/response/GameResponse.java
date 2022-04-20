package com.kdp.golf.websocket.response;

import com.kdp.golf.game.dto.GameDto;

public record GameResponse(GameDto game)
        implements Response {

    @Override
    public Type type() {
        return Type.Game;
    }
}
