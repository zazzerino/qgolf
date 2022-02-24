package com.kdp.golf.game.dto;

import com.kdp.golf.game.model.Card;
import com.kdp.golf.game.model.Player;

import java.util.Collection;
import java.util.Optional;

public record PlayerDto(Long id,
                        String name,
                        int visibleScore,
                        Collection<Card> cards,
                        Collection<Integer> uncovered,
                        Optional<Card> heldCard,
                        HandPosition handPosition) {

    public static PlayerDto from(Player p, HandPosition handPos) {
        return new PlayerDto(
                p.id(),
                p.name(),
                p.hand().visibleScore(),
                p.hand().cards(),
                p.hand().uncovered(),
                p.heldCard(),
                handPos);
    }
}
