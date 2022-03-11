package com.kdp.golf.game.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public interface GameEvent {

    @JsonProperty
    Type type();
    Long gameId();
    Long playerId();

    enum Type {
        UNCOVER,
        TAKE_FROM_DECK,
        TAKE_FROM_TABLE,
        SWAP_CARD,
        DISCARD,
    }

    record Uncover(Long gameId,
                   Long playerId,
                   int handIndex) implements GameEvent {
        @Override
        public Type type() {
            return Type.UNCOVER;
        }
    }

    record TakeFromDeck(Long gameId,
                        Long playerId) implements GameEvent {
        @Override
        public Type type() {
            return Type.TAKE_FROM_DECK;
        }
    }

    record TakeFromTable(Long gameId,
                         Long playerId) implements GameEvent {
        @Override
        public Type type() {
            return Type.TAKE_FROM_TABLE;
        }
    }

    record SwapCard(Long gameId,
                    Long playerId,
                    int handIndex) implements GameEvent {
        @Override
        public Type type() {
            return Type.SWAP_CARD;
        }
    }

    record Discard(Long gameId,
                   Long playerId) implements GameEvent {
        @Override
        public Type type() {
            return Type.DISCARD;
        }
    }
}