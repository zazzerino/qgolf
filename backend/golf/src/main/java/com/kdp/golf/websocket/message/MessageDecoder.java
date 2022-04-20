package com.kdp.golf.websocket.message;

import com.kdp.golf.game.model.GameEvent;
import io.vertx.core.json.JsonObject;

import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;
import java.util.Optional;

public class MessageDecoder implements Decoder.Text<Message> {

    @Override
    public Message decode(String s) throws DecodeException {
        var json = new JsonObject(s);
        var userId = json.getLong("userId");

        switch (typeOf(json)) {
            case UpdateName -> {
                var name = json.getString("name");
                return new UpdateNameMessage(userId, name);
            }
            case CreateGame -> {
                return new CreateGameMessage(userId);
            }
            case JoinGame -> {
                var gameId = json.getLong("gameId");
                return new JoinGameMessage(userId, gameId);
            }
            case StartGame -> {
                var gameId = json.getLong("gameId");
                return new StartGameMessage(userId, gameId);
            }
            case GameEvent -> {
                var gameId = json.getLong("gameId");
                var eventType = GameEvent.Type.valueOf(json.getString("eventType"));
                var handIndex = Optional.ofNullable(json.getInteger("handIndex"));
                return new GameEventMessage(userId, gameId, eventType, handIndex);
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
        return Message.Type.valueOf(
                json.getString("type"));
    }
}
