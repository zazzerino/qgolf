package com.kdp.golf.game.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.kdp.golf.Lib.Pair;
import org.immutables.value.Value;

import java.util.*;

@Value.Immutable
@JsonSerialize(as = ImmutableDeck.class)
public abstract class Deck {

    @Value.Parameter
    public abstract List<Card> cards();

    public static List<Card> cardList() {
        var ranks = Card.Rank.values();
        var suits = Card.Suit.values();
        var cards = new ArrayList<Card>();

        for (var rank : ranks) {
            for (var suit : suits) {
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

        return ImmutableDeck.of(cards);
    }

    public Deck shuffle() {
        var cards = new ArrayList<>(cards());
        Collections.shuffle(cards);
        return ImmutableDeck.of(cards);
    }

    public Pair<Optional<Card>, Deck> deal() {
        try {
            var cards = new ArrayDeque<>(cards());
            var card = Optional.of(cards.pop());
            var deck = ImmutableDeck.of(cards);
            return Pair.of(card, deck);
        } catch (NoSuchElementException _e) {
            return Pair.of(Optional.empty(), this);
        }
    }
}
