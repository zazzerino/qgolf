package com.kdp.golf.game.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.kdp.golf.Lib.Pair;
import org.immutables.value.Value;

import java.util.*;

@Value.Immutable
@JsonSerialize(as = ImmutableHand.class)
public abstract class Hand {

    @Value.Parameter
    public abstract List<Card> cards();
    @Value.Parameter
    public abstract Set<Integer> uncoveredCards();

    public static final int HAND_SIZE = 6;

    public static Hand empty() {
        return ImmutableHand.of(List.of(), Set.of());
    }

    public Hand uncover(int index) {
        return ImmutableHand.builder()
                .from(this)
                .addUncoveredCards(index)
                .build();
    }

    public Hand uncoverAll() {
        return ImmutableHand.copyOf(this)
                .withUncoveredCards(
                        Set.of(0, 1, 2, 3, 4, 5));
    }

    public boolean allCardsUncovered() {
        return uncoveredCards().size() == HAND_SIZE;
    }

    public Hand addCard(Card card) {
        if (cards().size() >= HAND_SIZE) {
            throw new IllegalStateException("hand can only hold a maximum of six cards");
        }

        return ImmutableHand.builder()
                .from(this)
                .addCards(card)
                .build();
    }

    public Pair<Optional<Card>, Hand> swapCard(Card newCard, int index) {
        var cards = new ArrayList<>(cards());
        var oldCard = Optional.of(cards.get(index));
        cards.set(index, newCard);
        var hand = ImmutableHand.copyOf(this)
                .withCards(cards);

        return Pair.of(oldCard, hand);
    }

//    public int visibleScore() {
//        var score = 0;
//
//        if (cards().size() != 6) {
//            return score;
//        }
//
//        var ranks = cards().stream().map(Card::rank).toList();
//        var uncoveredMap = new HashMap<Integer, Card>();
//
//        for (var i : uncoveredCards()) {
//            var card = cards().get(i);
//            uncoveredMap.put(i, card);
//        }
//
//        // check all six
//
//        // check outer four
//        var outerIndices = List.of(0, 2, 3, 5);
//
//        if (uncoveredMap.keySet().containsAll(outerIndices)
//                && Lib.indicesEqual(ranks, outerIndices)) {
//            score -= 50;
//            Lib.removeKeys(uncoveredMap, outerIndices);
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
//    }
}
