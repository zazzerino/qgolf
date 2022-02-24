package com.kdp.golf.game.dto;

import java.util.List;

enum HandPosition {
    BOTTOM,
    LEFT,
    TOP,
    RIGHT;

    /**
     * @return the positions of the hands on the screen from the user's point of view
     */
    public static List<HandPosition> positions(int playerCount) {
        return switch (playerCount) {
            case 1 -> List.of(BOTTOM);
            case 2 -> List.of(BOTTOM, TOP);
            case 3 -> List.of(BOTTOM, LEFT, RIGHT);
            case 4 -> List.of(BOTTOM, LEFT, TOP, RIGHT);
            default -> throw new IllegalStateException(
                    "playerCount must be between 1 and Game.MAX_PLAYERS: " + playerCount);
        };
    }
}
