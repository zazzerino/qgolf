package com.kdp.golf.game.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@JsonSerialize(using = ToStringSerializer.class, as = String.class)
public record Card(Rank rank,
                   Suit suit) {

    public static Card from(String s) {
        assert s.length() == 2;

        var chars = s.split("(?!^)");
        assert chars.length == 2;

        var rank = Rank.from(chars[0]).orElseThrow();
        var suit = Suit.from(chars[1]).orElseThrow();

        return new Card(rank, suit);
    }

    public static List<String> mapName(Collection<Card> cards) {
        return cards.stream()
                .map(Card::name)
                .toList();
    }

    public static List<Card> mapFrom(Collection<String> cardNames) {
        return cardNames.stream()
                .map(Card::from)
                .toList();
    }

    public String name() {
        return rank.value + suit.value;
    }

    public int golfValue() {
        return rank.golfValue();
    }

    @Override
    public String toString() {
        return name();
    }

    public enum Location {
        Deck,
        Table,
        Held,
        Hand0,
        Hand1,
        Hand2,
        Hand3,
        Hand4,
        Hand5
    }

    public enum Rank {
        ACE("A"),
        TWO("2"),
        THREE("3"),
        FOUR("4"),
        FIVE("5"),
        SIX("6"),
        SEVEN("7"),
        EIGHT("8"),
        NINE("9"),
        TEN("T"),
        JACK("J"),
        QUEEN("Q"),
        KING("K");

        public final String value;

        Rank(String value) {
            this.value = value;
        }

        public int golfValue() {
            return switch (this) {
                case KING -> 0;
                case ACE -> 1;
                case TWO -> 2;
                case THREE -> 3;
                case FOUR -> 4;
                case FIVE -> 5;
                case SIX -> 6;
                case SEVEN -> 7;
                case EIGHT -> 8;
                case NINE -> 9;
                case TEN, JACK, QUEEN -> 10;
            };
        }

        public static Optional<Rank> from(String s) {
            return Arrays.stream(Rank.values())
                    .filter(rank -> rank.value.equals(s))
                    .findAny();
        }
    }

    public enum Suit {
        CLUBS("C"),
        DIAMONDS("D"),
        HEARTS("H"),
        SPADES("S");

        public final String value;

        Suit(String value) {
            this.value = value;
        }

        public static Optional<Suit> from(String s) {
            return Arrays.stream(Suit.values())
                    .filter(suit -> suit.value.equals(s))
                    .findAny();
        }
    }
}