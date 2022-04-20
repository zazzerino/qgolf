package com.kdp.golf.websocket.message;

public record UpdateNameMessage(Long userId,
                                String name)
        implements Message {

    @Override
    public Type type() {
        return Type.UpdateName;
    }
}
