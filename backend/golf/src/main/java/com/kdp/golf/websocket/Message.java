package com.kdp.golf.websocket;

public interface Message {

    Type type();
    Long userId();

    enum Type {
        UpdateName,
        CreateGame,
        JoinGame,
        StartGame,
        GameEvent,
        Chat,
    }

    record UpdateName(Long userId, String name) implements Message {
        @Override
        public Type type() {
            return Type.UpdateName;
        }
    }

    record CreateGame(Long userId) implements Message {
        @Override
        public Type type() {
            return Type.CreateGame;
        }
    }

    record JoinGame(Long userId, Long gameId) implements Message {
        @Override
        public Type type() {
            return Type.JoinGame;
        }
    }

    record StartGame(Long userId, Long gameId) implements Message {
        @Override
        public Type type() {
            return Type.StartGame;
        }
    }
}
