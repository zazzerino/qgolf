package com.kdp.golf.websocket;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.kdp.golf.game.dto.GameDto;
import com.kdp.golf.user.UserDto;

public interface Response {

    @JsonProperty
    Type type();

    enum Type {
        User,
        Games,
        Game,
    }

    record User(UserDto user) implements Response {
        @Override
        public Type type() {
            return Type.User;
        }
    }

    record Game(GameDto game) implements Response {
        @Override
        public Type type() {
            return Type.Game;
        }
    }
}
