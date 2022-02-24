package com.kdp.golf.game.model;

import com.google.common.collect.Iterables;

import java.util.*;

public class Deck {

    private Deque<Card> cards;

    public Deck(Collection<Card> cards) {
        this.cards = new ArrayDeque<>(cards);
    }

    public static Deck create(int deckCount) {
        assert deckCount > 0;
        var cardList = enumerateCards();
        var cards = new ArrayDeque<>(cardList);

        for (var i = 1; i < deckCount; i++) {
            cards.addAll(cardList);
        }

        return new Deck(cards);
    }

    public static Collection<Card> enumerateCards() {
        var suits = Card.Suit.values();
        var ranks = Card.Rank.values();
        var cards = new ArrayDeque<Card>();

        for (var suit : suits) {
            for (var rank : ranks) {
                var card = new Card(rank, suit);
                cards.push(card);
            }
        }

        return cards;
    }

    public void shuffle() {
        var cardList = new ArrayList<>(cards);
        Collections.shuffle(cardList);
        cards = new ArrayDeque<>(cardList);
    }

    public Optional<Card> deal() {
        try {
            var card = cards.pop();
            return Optional.of(card);
        } catch (NoSuchElementException _e) {
            return Optional.empty();
        }
    }

    public Collection<Card> cards() { return cards; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Deck deck = (Deck) o;
        return Iterables.elementsEqual(cards, deck.cards);
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
