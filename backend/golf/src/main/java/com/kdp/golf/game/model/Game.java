package com.kdp.golf.game.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.collect.ImmutableMap;
import com.google.errorprone.annotations.Var;
import com.kdp.golf.Lib;
import org.immutables.value.Value;

import java.util.*;
import java.util.function.Predicate;

@Value.Immutable
@JsonSerialize(as = ImmutableGame.class)
public abstract class Game {

    public abstract Long id();
    public abstract Long hostId();
    public abstract State state();
    public abstract int turn();
    public abstract Deck deck();
    public abstract List<Card> tableCards();
    public abstract Map<Long, Player> players();
    public abstract List<Long> playerOrder();
    public abstract boolean isFinalTurn();

    public final static int DECK_COUNT = 2;
    public final static int MAX_PLAYERS = 4;

    public enum State {
        Init,
        UncoverTwo,
        Take,
        Discard,
        Uncover,
        GameOver,
    }

    public static Game create(Long id, Player host) {
        var hostId = host.id();
        return ImmutableGame.builder()
                .id(id)
                .hostId(hostId)
                .state(State.Init)
                .turn(0)
                .deck(Deck.create(DECK_COUNT))
                .tableCards(List.of())
                .players(Map.of(hostId, host))
                .playerOrder(List.of(hostId))
                .isFinalTurn(false)
                .build();
    }

    public Game addPlayer(Player player) {
        if (players().values().size() >= MAX_PLAYERS) {
            throw new IllegalStateException("can't have more than MAX_PLAYERS");
        }

        var id = player.id();
        return ImmutableGame.builder()
                .from(this)
                .putPlayers(id, player)
                .addPlayerOrder(id)
                .build();
    }

    public Game shuffleDeck() {
        var deck = deck().shuffle();
        return ImmutableGame.copyOf(this)
                .withDeck(deck);
    }

    public Game dealStartingHands() {
        @Var var deck = deck();
        var players = new HashMap<>(players());

        for (int i = 0; i < Hand.HAND_SIZE; i++) {
            for (var id : playerOrder()) {
                var pair = deck.deal();
                var card = pair.a().orElseThrow();
                deck = pair.b();

                var player = players.get(id).giveCard(card);
                players.put(id, player);
            }
        }

        return ImmutableGame.builder()
                .from(this)
                .deck(deck)
                .players(players)
                .build();
    }

    public Game dealTableCard() {
        var pair = deck().deal();
        var card = pair.a().orElseThrow();
        var deck = pair.b();

        return ImmutableGame.builder()
                .from(this)
                .addTableCards(card)
                .deck(deck)
                .build();
    }

    public Game start() {
        var game = shuffleDeck()
                .dealStartingHands()
                .dealTableCard();

        return ImmutableGame.copyOf(game)
                .withState(State.UncoverTwo);
    }

    public Game uncoverTwo(Long playerId, int handIndex) {
        Predicate<Player> stillUncovering = p -> p.uncoveredCardCount() < 2;
        @Var var player = players().get(playerId);

        if (stillUncovering.test(player)) {
            player = player.uncoverCard(handIndex);
        }

        var players = updatePlayer(player);
        var allReady = players.values().stream()
                .noneMatch(stillUncovering);

        var state = allReady ? State.Take : state();
        var turn = allReady ? turn() + 1 : turn();

        return ImmutableGame.builder()
                .from(this)
                .players(players)
                .state(state)
                .turn(turn)
                .build();
    }

    public Game uncover(Long playerId, int handIndex) {
        var player = players().get(playerId)
                .uncoverCard(handIndex);

        var players = updatePlayer(player);
        var turn = turn() + 1;

        return ImmutableGame.builder()
                .from(this)
                .players(players)
                .turn(turn)
                .build();
    }

    public Game takeFromDeck(Long playerId) {
        var pair = deck().deal();
        var card = pair.a().orElseThrow();
        var deck = pair.b();
        var player = players().get(playerId).holdCard(card);
        var players = updatePlayer(player);
        var turn = turn() + 1;

        return ImmutableGame.builder()
                .from(this)
                .deck(deck)
                .players(players)
                .state(State.Discard)
                .turn(turn)
                .build();
    }

    public Game takeFromTable(Long playerId) {
        var tableCards = new ArrayDeque<>(tableCards());
        var card = tableCards.pop();
        var player = players().get(playerId).holdCard(card);
        var players = updatePlayer(player);
        var turn = turn() + 1;

        return ImmutableGame.builder()
                .from(this)
                .tableCards(tableCards)
                .players(players)
                .state(State.Discard)
                .turn(turn)
                .build();
    }


    public Game discardHeldCard(Long playerId) {
        @Var var player = players().get(playerId);
        var pair = player.discardHeldCard();
        var card = pair.a().orElseThrow();
        player = pair.b();

        var tableCards = new ArrayDeque<>(tableCards());
        tableCards.push(card);

        var players = updatePlayer(player);

        return ImmutableGame.builder()
                .from(this)
                .players(players)
                .tableCards(tableCards)
                .build();
    }

    public Game swapCard(Long playerId, int handIndex) {
        var pair = players().get(playerId)
                .swapCard(handIndex);

        var card = pair.a().orElseThrow();
        var player = pair.b();

        var tableCards = new ArrayDeque<>(tableCards());
        tableCards.push(card);

        var players = updatePlayer(player);

        return ImmutableGame.builder()
                .from(this)
                .players(players)
                .tableCards(tableCards)
                .build();
    }


    public Game handleEvent(GameEvent event) {
        if (!playerCanAct(event.playerId())) {
            return this;
        }

        switch (state()) {
            case UncoverTwo -> {
                if (event instanceof GameEvent.Uncover e) {
                    return uncoverTwo(e.playerId(), e.handIndex());
                }
            }
            case Uncover -> {
                if (event instanceof GameEvent.Uncover e) {
                    return uncover(e.playerId(), e.handIndex());
                }
            }
            case Take -> {
                if (event instanceof GameEvent.TakeFromDeck e) {
                    return takeFromDeck(e.playerId());
                } else if (event instanceof GameEvent.TakeFromTable e) {
                    return takeFromTable(e.playerId());
                }
            }
        }

        throw new IllegalStateException(
                "Unexpected value. state: " + state() + " event: " + event);
    }

    public List<Long> playerOrderFrom(Long playerId) {
        var order = new ArrayList<>(playerOrder());
        var index = Lib.findIndex(order, playerId).orElseThrow();
        Collections.rotate(order, -index);
        return order;
    }

    /**
     * Returns the id of the player whose turn it is.
     */
    public Long playerTurn() {
        var index = turn() % players().size();
        return playerOrder().get(index);
    }

    public boolean playerCanAct(Long playerId) {
        return playerTurn().equals(playerId)
                || state() == State.UncoverTwo;
    }

    public List<Card.Location> playableCards(Long playerId) {
        if (!playerCanAct(playerId)) {
            return List.of();
        }

        return switch (state()) {
            case UncoverTwo, Uncover -> List.of(
                    Card.Location.Hand0,
                    Card.Location.Hand1,
                    Card.Location.Hand2,
                    Card.Location.Hand3,
                    Card.Location.Hand4,
                    Card.Location.Hand5);

            case Take -> List.of(Card.Location.Deck, Card.Location.Table);

            case Discard -> List.of(
                    Card.Location.Held,
                    Card.Location.Hand0,
                    Card.Location.Hand1,
                    Card.Location.Hand2,
                    Card.Location.Hand3,
                    Card.Location.Hand4,
                    Card.Location.Hand5);

            default -> List.of();
        };
    }

    private ImmutableMap<Long, Player> updatePlayer(Player player) {
        return Lib.updateMap(
                players(),
                Map.of(player.id(), player));
    }
}
