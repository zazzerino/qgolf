package com.kdp.golf.game.model;

import com.kdp.golf.lib.Pair;

import java.util.*;

public class Deck {

    private Deque<Card> cards;

    public static List<Card> cardList() {
        var suits = Card.Suit.values();
        var ranks = Card.Rank.values();
        var cards = new ArrayDeque<Card>();

        for (var suit : suits) {
            for (var rank : ranks) {
                var card = new Card(rank, suit);
                cards.add(card);
            }
        }

        return cards;
    }

    public Deck(Deque<Card> cards) {
        this.cards = cards;
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

//    public Deck shuffle() {
//        var cards = new ArrayList<>(cards());
//        Collections.shuffle(cards);
//        return new Deck(cards);
//    }
//
//    public Pair<Optional<Card>, Deck> deal() {
//        try {
//            var cardDeque = new ArrayDeque<>(cards());
//            var card = Optional.of(cardDeque.pop());
//            var cardList = List.copyOf(cardDeque);
//            var deck = new Deck(cardList);
//            return Pair.of(card, deck);
//        } catch (NoSuchElementException _e) {
//            return Pair.of(Optional.empty(), this);
//        }
//    }
}

//public record Deck(List<Card> cards) {
//
//    public static List<Card> cardList() {
//        var suits = Card.Suit.values();
//        var ranks = Card.Rank.values();
//        var cards = new ArrayList<Card>();
//
//        for (var suit : suits) {
//            for (var rank : ranks) {
//                var card = new Card(rank, suit);
//                cards.add(card);
//            }
//        }
//
//        return cards;
//    }
//
//    public static Deck create(int deckCount) {
//        assert deckCount > 0;
//        var cardList = cardList();
//        var cards = new ArrayList<>(cardList);
//
//        for (var i = 1; i < deckCount; i++) {
//            cards.addAll(cardList);
//        }
//
//        return new Deck(cards);
//    }
//
//    public Deck shuffle() {
//        var cards = new ArrayList<>(cards());
//        Collections.shuffle(cards);
//        return new Deck(cards);
//    }
//
//    public Pair<Optional<Card>, Deck> deal() {
//        try {
//            var cardDeque = new ArrayDeque<>(cards());
//            var card = Optional.of(cardDeque.pop());
//            var cardList = List.copyOf(cardDeque);
//            var deck = new Deck(cardList);
//            return Pair.of(card, deck);
//        } catch (NoSuchElementException _e) {
//            return Pair.of(Optional.empty(), this);
//        }
//    }
//}
