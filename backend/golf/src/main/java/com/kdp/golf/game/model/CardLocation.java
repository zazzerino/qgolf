package com.kdp.golf.game.model;

import java.util.List;

public enum CardLocation {
    DECK,
    TABLE,
    HELD,
    HAND_0,
    HAND_1,
    HAND_2,
    HAND_3,
    HAND_4,
    HAND_5;

    public static final List<CardLocation> UNCOVER_LOCATIONS = List.of(
            CardLocation.HAND_0,
            CardLocation.HAND_1,
            CardLocation.HAND_2,
            CardLocation.HAND_3,
            CardLocation.HAND_4,
            CardLocation.HAND_5);

    public static final List<CardLocation> TAKE_LOCATIONS = List.of(CardLocation.DECK, CardLocation.TABLE);

    public static final List<CardLocation> DISCARD_LOCATIONS = List.of(
            CardLocation.HELD,
            CardLocation.HAND_0,
            CardLocation.HAND_1,
            CardLocation.HAND_2,
            CardLocation.HAND_3,
            CardLocation.HAND_4,
            CardLocation.HAND_5);
}
