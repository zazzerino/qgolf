package com.kdp.golf.game.model.card;

import java.util.Arrays;
import java.util.Optional;

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
        return Arrays.stream(values())
                .filter(suit -> suit.value.equals(s))
                .findAny();
    }
}
