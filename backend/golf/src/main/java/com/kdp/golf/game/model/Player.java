package com.kdp.golf.game.model;

import com.kdp.golf.user.User;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.Optional;

public class Player {

    private final Long id;
    private final String name;
    private final Hand hand;
    private @Nullable Card heldCard;

    public Player(Long id, String name, Hand hand, @Nullable Card heldCard) {
        this.id = id;
        this.name = name;
        this.hand = hand;
        this.heldCard = heldCard;
    }

    public static Player create(Long id, String name) {
        return new Player(id, name, Hand.empty(), null);
    }

    public static Player from(User user) {
        return Player.create(user.id(), user.name());
    }

    public void giveCard(Card card) {
        hand.addCard(card);
    }

    public void uncoverCard(int handIndex) {
        hand.uncover(handIndex);
    }

    public void holdCard(Card card) {
        heldCard = card;
    }

    public Card discard() {
        if (heldCard == null) {
            throw new IllegalStateException("null heldCard");
        }

        var card = heldCard;
        heldCard = null;
        return card;
    }

    public Card swapCard(int handIndex) {
        if (heldCard == null) {
            throw new IllegalStateException("null heldCard");
        }

        var card = heldCard;
        heldCard = null;
        return hand.swapCard(card, handIndex);
    }

    public int uncoveredCount() {
        return hand.uncoveredCards().size();
    }

    public Long id() { return id; }

    public String name() { return name; }

    public Hand hand() { return hand; }

    public Optional<Card> heldCard() {
        return Optional.ofNullable(heldCard);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return id.equals(player.id)
                && name.equals(player.name)
                && hand.equals(player.hand)
                && Objects.equals(heldCard, player.heldCard);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, hand, heldCard);
    }

    @Override
    public String toString() {
        return "Player{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", hand=" + hand +
                ", heldCard=" + heldCard +
                '}';
    }
}

