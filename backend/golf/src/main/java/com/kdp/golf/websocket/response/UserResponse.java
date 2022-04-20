package com.kdp.golf.websocket.response;

import com.kdp.golf.user.UserDto;

public record UserResponse(UserDto user)
        implements Response {

    @Override
    public Type type() {
        return Type.User;
    }
}
