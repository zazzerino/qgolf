package com.kdp.golf.game;

import com.kdp.golf.game.model.Card;
import com.kdp.golf.game.model.Game;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Set;

public record GameDto(Long id,
                      Long userId,
                      Long hostId,
                      Game.State state,
                      int turn,
                      Long playerTurn,
                      List<Card> tableCards,
                      List<Card> playableCards,
                      List<PlayerDto> players) {

    enum HandPosition { Bottom, Left, Top, Right, }

    public record PlayerDto(Long id,
                            String name,
                            HandPosition handPosition,
                            List<Card> cards,
                            Set<Integer> uncoveredCards,
                            @Nullable Card heldCard,
                            int score) {
    }

    public static GameDto from(Game game, Long userId) {
        return null;
    }

    public static List<HandPosition> handPositions(int playerCount) {
        return switch (playerCount) {
            case 1 -> List.of(HandPosition.Bottom);
            case 2 -> List.of(HandPosition.Bottom, HandPosition.Top);
            case 3 -> List.of(HandPosition.Bottom, HandPosition.Left, HandPosition.Right);
            case 4 -> List.of(HandPosition.Bottom, HandPosition.Left, HandPosition.Top, HandPosition.Right);
            default -> throw new IllegalStateException(
                    "playerCount must be between 1 and 4: " + playerCount);
        };
    }
}
