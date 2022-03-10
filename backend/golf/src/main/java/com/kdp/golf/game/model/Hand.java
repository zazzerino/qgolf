package com.kdp.golf.game.model;

import com.google.common.collect.Iterables;

import java.util.*;

public class Hand {

    private final List<Card> cards;
    private final Set<Integer> uncovered;

    public static final int HAND_SIZE = 6;

    public Hand(List<Card> cards, Set<Integer> uncovered) {
        this.cards = new ArrayList<>(cards);
        this.uncovered = new HashSet<>(uncovered);
    }

    public static Hand empty() {
        return new Hand(
                Collections.emptyList(),
                Collections.emptySet());
    }

    public void uncover(int index) {
        uncovered.add(index);
    }

    public void uncoverAll() {
        uncovered.addAll(Set.of(0, 1, 2, 3, 4, 5));
    }

    public boolean allCardsUncovered() {
        return uncovered().size() == HAND_SIZE;
    }

    public void addCard(Card card) {
        if (cards.size() >= HAND_SIZE) {
            throw new IllegalStateException("hand can only hold a maximum of six cards");
        }

        cards.add(card);
    }

    public Card swapCard(Card newCard, int index) {
        var oldCard = cards.get(index);
        cards.set(index, newCard);
        return oldCard;
    }

    public int visibleScore() {
//        var score = 0;
//
//        if (cards().size() != 6) {
//            return score;
//        }
//
//        var ranks = cards().stream().map(Card::rank).toList();
//        var uncoveredMap = uncovered().stream()
//                .collect(Collectors.toMap(
//                        Function.identity(),
//                        i -> cards().get(i),
//                        (_prev, next) -> next,
//                        HashMap::new));
//
//        // check all six
//
//        // check outer four
//        var outerIndices = List.of(0, 2, 3, 5);
//
//        if (uncoveredMap.keySet().containsAll(outerIndices)
//                && Lib.indicesEqual(ranks, outerIndices)) {
//            score -= 50;
//            Lib.removeKeysDestructive(uncoveredMap, outerIndices);
//        }
//
//        // check left four
//
//        // check right four
//
//        // check left column
//        var leftCol = List.of(0, 3);
//
//        if (uncoveredMap.keySet().containsAll(leftCol)
//                && Lib.indicesEqual(ranks, leftCol)) {
//            Lib.removeKeys(uncoveredMap, leftCol);
//        }
//
//        // check middle column
//        var middleCol = List.of(1, 4);
//
//        if (uncoveredMap.keySet().containsAll(middleCol)
//                && Lib.indicesEqual(ranks, middleCol)) {
//            Lib.removeKeys(uncoveredMap, middleCol);
//        }
//
//        // check right column
//        var rightCol = List.of(2, 5);
//
//        if (uncoveredMap.keySet().containsAll(rightCol)
//                && Lib.indicesEqual(ranks, rightCol)) {
//            Lib.removeKeys(uncoveredMap, rightCol);
//        }
//
//        // sum remaining cards
//        for (var card : uncoveredMap.values()) {
//            score += card.golfValue();
//        }
//
//        return score;
        return 0;
    }

    public List<Card> cards() { return cards; }

    public Set<Integer> uncovered() { return uncovered; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Hand hand = (Hand) o;
        return Iterables.elementsEqual(cards, hand.cards)
                && Iterables.elementsEqual(uncovered, hand.uncovered);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cards, uncovered);
    }

    @Override
    public String toString() {
        return "Hand{" +
                "cards=" + cards +
                ", uncovered=" + uncovered +
                '}';
    }
}
