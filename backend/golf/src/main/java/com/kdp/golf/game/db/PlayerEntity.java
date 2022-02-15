package com.kdp.golf.game.db;

import com.kdp.golf.game.model.Hand;
import com.kdp.golf.game.model.Player;
import com.kdp.golf.game.model.Card;
import org.jetbrains.annotations.Nullable;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.*;

@Entity(name = "Player")
@Table(name = "player")
public class PlayerEntity {

    public @Id Long id;
    public String name;
    public @ElementCollection List<String> handCards;
    public @ElementCollection Set<Integer> uncoveredCards;
    public @Nullable String heldCard;

    @ManyToOne(fetch = FetchType.LAZY)
    public GameEntity game;

    public PlayerEntity() {}

    public PlayerEntity(Long id,
                        String name,
                        List<String> handCards,
                        Set<Integer> uncoveredCards,
                        @Nullable String heldCard,
                        GameEntity game) {
        this.id = id;
        this.name = name;
        this.handCards = handCards;
        this.uncoveredCards = uncoveredCards;
        this.heldCard = heldCard;
        this.game = game;
    }

    public static PlayerEntity from(Player p, GameEntity game) {
        var handCards = Card.mapName(p.hand().cards());
        var uncoveredCards = p.hand().uncoveredCards();

        var heldCard = p.heldCard()
                .map(Card::name)
                .orElse(null);

        return new PlayerEntity(
                p.id(),
                p.name(),
                handCards,
                uncoveredCards,
                heldCard,
                game);
    }

    public Player toPlayer() {
        var cards = Card.mapFrom(handCards);
        var hand = new Hand(cards, uncoveredCards);
        var card = heldCard  != null
                ? Card.from(heldCard)
                : null;

        return new Player(id, name, hand, card);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlayerEntity that = (PlayerEntity) o;
        return id.equals(that.id)
                && name.equals(that.name)
                && handCards.equals(that.handCards)
                && uncoveredCards.equals(that.uncoveredCards)
                && Objects.equals(heldCard, that.heldCard);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, handCards, uncoveredCards, heldCard);
    }

    @Override
    public String toString() {
        return "PlayerEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", handCards=" + handCards +
                ", uncoveredCards=" + uncoveredCards +
                ", heldCard='" + heldCard + '\'' +
                '}';
    }
}
