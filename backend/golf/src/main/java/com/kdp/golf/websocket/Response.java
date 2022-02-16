package com.kdp.golf.websocket;

import com.fasterxml.jackson.annotation.JsonProperty;

public interface Response {

    @JsonProperty
    Type type();

    enum Type {
        User,
        Games,
        Game,
    }

    record User(com.kdp.golf.user.User user) implements Response {
        @Override
        public Type type() {
            return Type.User;
        }
    }
}
