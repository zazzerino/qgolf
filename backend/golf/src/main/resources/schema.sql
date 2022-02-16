DROP TABLE IF EXISTS player;
DROP TABLE IF EXISTS game;
DROP TABLE IF EXISTS person;

-- The 'person' table represents a user.
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
    deck         TEXT[]    NOT NULL,
    table_cards  TEXT[]    NOT NULL,
    player_order BIGINT[]  NOT NULL
);

CREATE TABLE player
(
    game            BIGINT NOT NULL,
    person          BIGINT NOT NULL,
    hand_cards      TEXT[] NOT NULL,
    uncovered_cards INT[]  NOT NULL,
    held_card       TEXT,
    PRIMARY KEY (game, person)
);
