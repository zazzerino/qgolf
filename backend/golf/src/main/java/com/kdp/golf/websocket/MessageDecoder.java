package com.kdp.golf.websocket;

import io.vertx.core.json.JsonObject;

import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;

public class MessageDecoder implements Decoder.Text<Message> {

    @Override
    public Message decode(String s) throws DecodeException {
        var json = new JsonObject(s);
        var userId = json.getLong("userId");
        var messageType = typeOf(json);

        switch (messageType) {
            case UpdateName -> {
                var name = json.getString("name");
                return new Message.UpdateName(userId, name);
            }
            case CreateGame -> {
                return new Message.CreateGame(userId);
            }
            case JoinGame -> {
                var gameId = json.getLong("gameId");
                return new Message.JoinGame(userId, gameId);
            }
            case StartGame -> {
                var gameId = json.getLong("gameId");
                return new Message.StartGame(userId, gameId);
            }
        }

        throw new DecodeException(s, "could not decode message");
    }

    @Override
    public boolean willDecode(String s) {
        var json = new JsonObject(s);
        return typeOf(json) != null;
    }

    @Override
    public void init(EndpointConfig config) {}

    @Override
    public void destroy() {}

    private Message.Type typeOf(JsonObject json) {
        var type = json.getString("type");
        return Message.Type.valueOf(type);
    }
}