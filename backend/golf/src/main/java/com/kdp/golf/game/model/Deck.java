package com.kdp.golf.game.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class Deck {

    private List<Card> cards;

    public Deck (List<Card> cards) {
        this.cards = new ArrayList<>(cards);
    }

    public static List<Card> cardList() {
        var cards = new ArrayList<Card>();

        for (var suit : Card.Suit.values()) {
            for (var rank : Card.Rank.values()) {
                var card = new Card(rank, suit);
                cards.add(card);
            }
        }

        return cards;
    }

    public static Deck create(int deckCount) {
        assert deckCount > 0;

        var cardList = cardList();
        var cards = new ArrayList<>(cardList);

        for (var i = 1; i < deckCount; i++) {
            cards.addAll(cardList);
        }

        return new Deck(cards);
    }

    public void shuffle() {
        var cardsCopy = new ArrayList<>(cards);
        Collections.shuffle(cardsCopy);
        cards = cardsCopy;
    }

    public Optional<Card> deal() {
        try {
            var card = cards.remove(0);
            return Optional.of(card);
        } catch (Exception _e) {
            return Optional.empty();
        }
    }

    public Deck copy() {
        return new Deck(cards);
    }

    @JsonProperty
    public List<Card> cards() { return cards; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Deck deck = (Deck) o;
        return cards.equals(deck.cards);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cards);
    }

    @Override
    public String toString() {
        return "Deck{" +
                "cards=" + cards +
                '}';
    }
}
