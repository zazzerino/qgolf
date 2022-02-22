package com.kdp.golf.game.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import java.util.Arrays;
import java.util.Optional;

@JsonSerialize(as = String.class, using = ToStringSerializer.class)
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
        Ace("A"),
        Two("2"),
        Three("3"),
        Four("4"),
        Five("5"),
        Six("6"),
        Seven("7"),
        Eight("8"),
        Nine("9"),
        Ten("T"),
        Jack("J"),
        Queen("Q"),
        King("K");

        public final String value;

        Rank(String value) {
            this.value = value;
        }

        public int golfValue() {
            return switch (this) {
                case King -> 0;
                case Ace -> 1;
                case Two -> 2;
                case Three -> 3;
                case Four -> 4;
                case Five -> 5;
                case Six -> 6;
                case Seven -> 7;
                case Eight -> 8;
                case Nine -> 9;
                case Ten, Jack, Queen -> 10;
            };
        }

        public static Optional<Rank> from(String s) {
            return Arrays.stream(Rank.values())
                    .filter(rank -> rank.value.equals(s))
                    .findAny();
        }
    }

    public enum Suit {
        Clubs("C"),
        Diamonds("D"),
        Hearts("H"),
        Spades("S");

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