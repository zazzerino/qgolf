package com.kdp.golf.websocket.message;

import com.kdp.golf.game.model.GameEvent;

import java.util.Optional;

public record GameEventMessage(Long userId,
                               Long gameId,
                               GameEvent.Type eventType,
                               Optional<Integer> handIndex)
        implements Message {

    @Override
    public Message.Type type() {
        return Message.Type.GameEvent;
    }

    public GameEvent gameEvent() {
        return switch (eventType) {
            case UNCOVER -> new GameEvent.Uncover(gameId, userId, handIndex.orElseThrow());
            case TAKE_FROM_DECK -> new GameEvent.TakeFromDeck(gameId, userId);
            case TAKE_FROM_TABLE -> new GameEvent.TakeFromTable(gameId, userId);
            case SWAP_CARD -> new GameEvent.SwapCard(gameId, userId, handIndex.orElseThrow());
            case DISCARD -> new GameEvent.Discard(gameId, userId);
        };
    }
}
