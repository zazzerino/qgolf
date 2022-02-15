package com.kdp.golf.game.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.kdp.golf.Lib;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class Hand {

    private final List<Card> cards;
    private final Set<Integer> uncoveredCards;

    public static final int HAND_SIZE = 6;

    public Hand(List<Card> cards, Set<Integer> uncoveredCards) {
        this.cards = new ArrayList<>(cards);
        this.uncoveredCards = new HashSet<>(uncoveredCards);
    }

    public Hand(List<Card> cards) {
        this.cards = new ArrayList<>(cards);
        this.uncoveredCards = new HashSet<>(HAND_SIZE);
    }

    public static Hand empty() {
        var cards = new ArrayList<Card>(HAND_SIZE);
        var uncoveredCards = new HashSet<Integer>(HAND_SIZE);

        return new Hand(cards, uncoveredCards);
    }

    public static Hand of(String... cardNames) {
        var cards = Arrays.stream(cardNames)
                .map(Card::from)
                .toList();

        return new Hand(cards);
    }

    public void uncover(int index) {
        uncoveredCards.add(index);
    }

    public void uncoverAll() {
        uncoveredCards.addAll(Set.of(0, 1, 2, 3, 4, 5));
    }

    public boolean allCardsUncovered() {
        return uncoveredCards.size() == HAND_SIZE;
    }

    public void addCard(Card card) {
        if (cards.size() >= HAND_SIZE) {
            throw new IllegalStateException("hand can only hold six cards max");
        }

        cards.add(card);
    }

    public Card swapCard(Card newCard, int index) {
        var card = cards.get(index);
        cards.set(index, newCard);
        return card;
    }

    @JsonProperty
    public int visibleScore() {
        var score = 0;

        if (cards.size() != 6) {
            return score;
        }

        var ranks = cards.stream().map(Card::rank).toList();
        var uncoveredMap = new HashMap<Integer, Card>();

        for (var i : uncoveredCards) {
            var card = cards.get(i);
            uncoveredMap.put(i, card);
        }

        // check all six

        // check outer four
        var outerIndices = List.of(0, 2, 3, 5);

        if (uncoveredMap.keySet().containsAll(outerIndices)
                && Lib.indicesEqual(ranks, outerIndices)) {
            score -= 50;
            Lib.removeKeys(uncoveredMap, outerIndices);
        }

        // check left four

        // check right four

        // check left column
        var leftCol = List.of(0, 3);

        if (uncoveredMap.keySet().containsAll(leftCol)
                && Lib.indicesEqual(ranks, leftCol)) {
            Lib.removeKeys(uncoveredMap, leftCol);
        }

        // check middle column
        var middleCol = List.of(1, 4);

        if (uncoveredMap.keySet().containsAll(middleCol)
                && Lib.indicesEqual(ranks, middleCol)) {
            Lib.removeKeys(uncoveredMap, middleCol);
        }

        // check right column
        var rightCol = List.of(2, 5);

        if (uncoveredMap.keySet().containsAll(rightCol)
                && Lib.indicesEqual(ranks, rightCol)) {
            Lib.removeKeys(uncoveredMap, rightCol);
        }

        // sum remaining cards
        for (var card : uncoveredMap.values()) {
            score += card.golfValue();
        }

        return score;
    }

    @JsonProperty
    public List<Card> cards() { return cards; }

    @JsonProperty
    public Set<Integer> uncoveredCards() { return uncoveredCards; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Hand hand = (Hand) o;
        return cards.equals(hand.cards) && uncoveredCards.equals(hand.uncoveredCards);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cards, uncoveredCards);
    }

    @Override
    public String toString() {
        return "Hand{" +
                "cards=" + cards +
                ", uncoveredCards=" + uncoveredCards +
                '}';
    }
}
