package com.kdp.golf.game.model.card;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(using = CardSerializer.class)
public record Card(Rank rank,
                   Suit suit) {

    public static Card from(String s) {
        assert s.length() == 2;

        var chars = s.split("(?!^)");
        assert chars.length == 2;

        var rank = Rank.from(chars[0]).orElseThrow();
        var suit = Suit.from(chars[1]).orElseThrow();

        return new Card (rank, suit);
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
}