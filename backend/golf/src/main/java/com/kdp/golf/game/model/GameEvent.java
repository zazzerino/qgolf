package com.kdp.golf.game.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public interface GameEvent {

    @JsonProperty
    Type type();
    Long gameId();
    Long playerId();

    enum Type {
        Uncover,
        TakeFromDeck,
        TakeFromTable,
        SwapCard,
        Discard,
    }

    record Uncover(Long gameId,
                   Long playerId,
                   int handIndex) implements GameEvent {
        @Override
        public Type type() {
            return Type.Uncover;
        }
    }

    record TakeFromDeck(Long gameId,
                        Long playerId) implements GameEvent {
        @Override
        public Type type() {
            return Type.TakeFromDeck;
        }
    }

    record TakeFromTable(Long gameId,
                         Long playerId) implements GameEvent {
        @Override
        public Type type() {
            return Type.TakeFromTable;
        }
    }

    record SwapCard(Long gameId,
                    Long playerId,
                    int handIndex) implements GameEvent {
        @Override
        public Type type() {
            return Type.SwapCard;
        }
    }

    record Discard(Long gameId,
                   Long playerId) implements GameEvent {
        @Override
        public Type type() {
            return Type.Discard;
        }
    }
}