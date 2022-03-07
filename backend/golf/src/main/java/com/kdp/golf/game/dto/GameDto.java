package com.kdp.golf.game.dto;

import com.kdp.golf.game.model.Card;
import com.kdp.golf.game.model.Game;
import com.kdp.golf.game.model.CardLocation;

import java.util.*;

public record GameDto(Long id,
                      Long userId,
                      Long hostId,
                      Game.State state,
                      int turn,
                      Long playerTurn,
                      Collection<Card> tableCards,
                      Collection<CardLocation> playableCards,
                      Collection<PlayerDto> players) {

    public static GameDto from(Game g, Long userId) {
        var userPlayer = g.getPlayer(userId).orElseThrow();
        var playableCards = g.playableCards(userPlayer);

        var playerCount = g.players().size();
        var positions = HandPosition.positions(playerCount);
        var playerOrder = g.playerOrderFrom(userId);
        var playerDtos = new ArrayList<PlayerDto>();

        for (var i = 0; i < playerCount; i++) {
            var id = playerOrder.get(i);
            var player = g.getPlayer(id).orElseThrow();
            var handPos = positions.get(i);
            var dto = PlayerDto.from(player, handPos);
            playerDtos.add(dto);
        }

        return new GameDto(
                g.id(),
                userId,
                g.hostId(),
                g.state(),
                g.turn(),
                g.playerTurn(),
                g.tableCards(),
                playableCards,
                playerDtos);
    }
}
