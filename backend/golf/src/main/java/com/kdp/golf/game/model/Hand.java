package com.kdp.golf.game.model;

import com.kdp.golf.lib.Lib;
import com.kdp.golf.lib.Pair;

import java.util.*;

public record Hand(List<Card> cards,
                   Set<Integer> uncovered) {

    public static final int HAND_SIZE = 6;

    public static Hand empty() {
        return new Hand(List.of(), Set.of());
    }

    public Hand uncover(int index) {
        var uncovered = Lib.setWithElem(uncovered(), index);
        return withUncovered(uncovered);
    }

    public Hand uncoverAll() {
        var uncovered = Set.of(0, 1, 2, 3, 4, 5);
        return withUncovered(uncovered);
    }

    public boolean allCardsUncovered() {
        return uncovered().size() == HAND_SIZE;
    }

    public Hand addCard(Card card) {
        if (cards().size() >= HAND_SIZE) {
            throw new IllegalStateException("hand can only hold a maximum of six cards");
        }

        var cards = Lib.listWithElem(cards(), card);
        return withCards(cards);
    }

    public Pair<Optional<Card>, Hand> swapCard(Card newCard, int index) {
        var cards = new ArrayList<>(cards());
        var oldCard = Optional.ofNullable(cards.get(index));
        cards.set(index, newCard);

        var hand = withCards(cards);
        return Pair.of(oldCard, hand);
    }

    public Hand withCards(List<Card> cards) {
        return new Hand(cards, uncovered);
    }

    public Hand withUncovered(Set<Integer> uncovered) {
        return new Hand(cards, uncovered);
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
}

