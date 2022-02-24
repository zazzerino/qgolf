DROP TABLE IF EXISTS player;
DROP TABLE IF EXISTS game;
DROP TABLE IF EXISTS person;

-- The 'user' table represents a user.
-- The name 'person' is used because 'user' would conflict with a builtin postgres table.
CREATE TABLE person
(
    id         BIGSERIAL PRIMARY KEY,
    name       TEXT      NOT NULL,
    session_id TEXT      NOT NULL
);

CREATE TABLE game
(
    id           BIGSERIAL PRIMARY KEY,
    host         BIGINT    NOT NULL,
    state        TEXT      NOT NULL,
    turn         INT       NOT NULL,
    final_turn   BOOLEAN   NOT NULL,
    deck         TEXT[]    NOT NULL,
    table_cards  TEXT[]    NOT NULL,
    players      BIGINT[]  NOT NULL
);

CREATE TABLE player
(
    game            BIGINT NOT NULL,
    person          BIGINT NOT NULL,
    hand_cards      TEXT[] NOT NULL,
    uncovered       INT[]  NOT NULL,
    held_card       TEXT,
    PRIMARY KEY (game, person)
);
