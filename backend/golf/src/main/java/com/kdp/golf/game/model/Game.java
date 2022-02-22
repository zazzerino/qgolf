package com.kdp.golf.game.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.collect.ImmutableMap;
import com.google.errorprone.annotations.Var;
import com.kdp.golf.lib.Lib;

import java.util.*;
import java.util.function.Predicate;

public class Game {

    private Long id;
    private Long hostId;
    private State state;
    private int turn;
    private Deck deck;
    private Deque<Card> tableCards;
    private Map<Long, Player> players;

    public enum State {
        Init,
        UncoverTwo,
        Take,
        Discard,
        Uncover,
        GameOver,
    }

}

//public record Game(Long id,
//                   Long hostId,
//                   State state,
//                   int turn,
//                   Deck deck,
//                   Deque<Card> tableCards,
//                   Map<Long, Player> players,
//                   boolean isFinalTurn) {
//
//    public enum State {
//        Init,
//        UncoverTwo,
//        Take,
//        Discard,
//        Uncover,
//        GameOver,
//    }
//
//    public static final int DECK_COUNT = 2;
//    public static final int MAX_PLAYERS = 4;
//
//    public static Game create(Long id, Player host) {
//        var state = State.Init;
//        var turn = 0;
//        var deck = Deck.create(DECK_COUNT);
////        var tableCards = List.<Card>of();
//        var isFinalTurn = false;
//
//        var players = new LinkedHashMap<Long, Player>();
//        players.put(host.id(), host);
//
//        return new Game(id, host.id(), state, turn, deck, tableCards, players, isFinalTurn);
//    }
//
//    public Game addPlayer(Player player) {
//        if (players().values().size() >= MAX_PLAYERS) {
//            throw new IllegalStateException("can't have more than MAX_PLAYERS");
//        }
//
//        var players = new LinkedHashMap<>(players());
//        players.put(player.id(), player);
//
//        return withPlayers(players);
//    }
//
//    public Game shuffleDeck() {
//        return withDeck(deck.shuffle());
//    }
//
//    public Game dealStartingHands() {
//        @Var var deck = deck();
//        var players = new LinkedHashMap<>(players());
//
//        for (int i = 0; i < Hand.HAND_SIZE; i++) {
//            for (var id : players().keySet()) {
//                var pair = deck.deal();
//                var card = pair.a().orElseThrow();
//                deck = pair.b();
//
//                var player = players.get(id).giveCard(card);
//                players.put(id, player);
//            }
//        }
//
//        return new Game(id, hostId, state, turn, deck, tableCards, players, isFinalTurn);
//    }
//
//    public Game dealTableCard() {
//        var pair = deck().deal();
//        var card = pair.a().orElseThrow();
//        var deck = pair.b();
//
//        var tableCards = new ArrayList<>(tableCards());
//        tableCards.add(card);
//
//        return new Game(id, hostId, state, turn, deck, tableCards, players, isFinalTurn);
//    }
//
//    public Game start() {
//        return shuffleDeck()
//                .dealStartingHands()
//                .dealTableCard()
//                .withState(State.UncoverTwo);
//    }
//
//    public Game uncoverTwo(Long playerId, int handIndex) {
//        Predicate<Player> stillUncovering = p -> p.uncoveredCardCount() < 2;
//        @Var var player = players().get(playerId);
//
//        if (stillUncovering.test(player)) {
//            player = player.uncoverCard(handIndex);
//        }
//
//        var players = updatePlayers(player);
//        var allReady = players.values()
//                .stream()
//                .noneMatch(stillUncovering);
//
//        var state = allReady ? State.Take : state();
//        var turn = allReady ? turn() + 1 : turn();
//
//        return new Game(id, hostId, state, turn, deck, tableCards, players, isFinalTurn);
//    }
//
//    public Game uncover(Long playerId, int handIndex) {
//        var player = players().get(playerId).uncoverCard(handIndex);
//        var players = updatePlayers(player);
//        var turn = turn() + 1;
//
//        return new Game(id, hostId, state, turn, deck, tableCards, players, isFinalTurn);
//    }
//
//    public Game takeFromDeck(Long playerId) {
//        var pair = deck().deal();
//        var card = pair.a().orElseThrow();
//        var deck = pair.b();
//
//        var player = players().get(playerId).holdCard(card);
//        var players = updatePlayers(player);
//        var state = State.Discard;
//        var turn = turn() + 1;
//
//        return new Game(id, hostId, state, turn, deck, tableCards, players, isFinalTurn);
//    }
//
//    public Game takeFromTable(Long playerId) {
//        var tableCardDeque = new ArrayDeque<>(tableCards());
//        var card = tableCardDeque.pop();
//        var tableCards = List.copyOf(tableCardDeque);
//
//        var player = players().get(playerId).holdCard(card);
//        var players = updatePlayers(player);
//
//        var state = State.Discard;
//        var turn = turn() + 1;
//
//        return new Game(id, hostId, state, turn, deck, tableCards, players, isFinalTurn);
//    }
//
//    public Game discardHeldCard(Long playerId) {
//        return null;
////        @Var var player = players().get(playerId);
////        var pair = player.discardHeldCard();
////        var card = pair.a().orElseThrow();
////        player = pair.b();
////
////        var tableCards = new ArrayDeque<>(tableCards());
////        tableCards.push(card);
////
////        var players = updatePlayers(player);
//
////        return ImmutableGame.builder()
////                .from(this)
////                .players(players)
////                .tableCards(tableCards)
////                .build();
//    }
//
//    public Game swapCard(Long playerId, int handIndex) {
//        return null;
////        var pair = players().get(playerId)
////                .swapCard(handIndex);
////
////        var card = pair.a().orElseThrow();
////        var player = pair.b();
////
////        var tableCards = new ArrayDeque<>(tableCards());
////        tableCards.push(card);
////
////        var players = updatePlayers(player);
////        return ImmutableGame.builder()
////                .from(this)
////                .players(players)
////                .tableCards(tableCards)
////                .build();
//    }
//
//
//    public Game handleEvent(GameEvent event) {
//        if (!playerCanAct(event.playerId())) {
//            return this;
//        }
//
//        switch (state()) {
//            case UncoverTwo -> {
//                if (event instanceof GameEvent.Uncover e) {
//                    return uncoverTwo(e.playerId(), e.handIndex());
//                }
//            }
//            case Uncover -> {
//                if (event instanceof GameEvent.Uncover e) {
//                    return uncover(e.playerId(), e.handIndex());
//                }
//            }
//            case Take -> {
//                if (event instanceof GameEvent.TakeFromDeck e) {
//                    return takeFromDeck(e.playerId());
//                } else if (event instanceof GameEvent.TakeFromTable e) {
//                    return takeFromTable(e.playerId());
//                }
//            }
//        }
//
//        throw new IllegalStateException(
//                "Unexpected value. state: " + state() + " event: " + event);
//    }
//
//    public List<Long> playerOrderFrom(Long playerId) {
////        var order = new ArrayList<>(playerOrder());
////        var index = Lib.findIndex(order, playerId).orElseThrow();
////        Collections.rotate(order, -index);
////        return order;
//        return null;
//    }
//
//    /**
//     * Returns the id of the player whose turn it is.
//     */
//    public Long playerTurn() {
////        var index = turn() % players().size();
////        return playerOrder().get(index);
//        return null;
//    }
//
//    public boolean playerCanAct(Long playerId) {
//        return playerTurn().equals(playerId)
//                || state() == State.UncoverTwo;
//    }
//
//    public List<Card.Location> playableCards(Long playerId) {
//        if (!playerCanAct(playerId)) {
//            return List.of();
//        }
//
//        return switch (state()) {
//            case UncoverTwo, Uncover -> List.of(
//                    Card.Location.Hand0,
//                    Card.Location.Hand1,
//                    Card.Location.Hand2,
//                    Card.Location.Hand3,
//                    Card.Location.Hand4,
//                    Card.Location.Hand5);
//
//            case Take -> List.of(Card.Location.Deck, Card.Location.Table);
//
//            case Discard -> List.of(
//                    Card.Location.Held,
//                    Card.Location.Hand0,
//                    Card.Location.Hand1,
//                    Card.Location.Hand2,
//                    Card.Location.Hand3,
//                    Card.Location.Hand4,
//                    Card.Location.Hand5);
//
//            default -> List.of();
//        };
//    }
//
//    private Map<Long, Player> updatePlayers(Player player) {
//        return Lib.updateMap(players(), Map.of(player.id(), player));
//    }
//
//    public Game withPlayers(Map<Long, Player> players) {
//        return new Game(id, hostId, state, turn, deck, tableCards, players, isFinalTurn);
//    }
//
//    public Game withDeck(Deck deck) {
//        return new Game(id, hostId, state, turn, deck, tableCards, players, isFinalTurn);
//    }
//
//    public Game withState(State state) {
//        return new Game(id, hostId, state, turn, deck, tableCards, players, isFinalTurn);
//    }
//}

//@Value.Immutable
//@JsonSerialize(as = ImmutableGame.class)
//public abstract class Game {
//
//    public abstract Long id();
//    public abstract Long hostId();
//    public abstract State state();
//    public abstract int turn();
//    public abstract Deck deck();
//    public abstract List<Card> tableCards();
//    public abstract Map<Long, Player> players();
//    public abstract List<Long> playerOrder();
//    public abstract boolean isFinalTurn();
//
//    public static final int DECK_COUNT = 2;
//    public static final int MAX_PLAYERS = 4;
//
//    public enum State {
//        Init,
//        UncoverTwo,
//        Take,
//        Discard,
//        Uncover,
//        GameOver,
//    }
//
//    public static Game create(Long id, Player host) {
//        var hostId = host.id();
//        return ImmutableGame.builder()
//                .id(id)
//                .hostId(hostId)
//                .state(State.Init)
//                .turn(0)
//                .deck(Deck.create(DECK_COUNT))
//                .tableCards(List.of())
//                .players(Map.of(hostId, host))
//                .playerOrder(List.of(hostId))
//                .isFinalTurn(false)
//                .build();
//    }
//
//    public Game addPlayer(Player player) {
//        if (players().values().size() >= MAX_PLAYERS) {
//            throw new IllegalStateException("can't have more than MAX_PLAYERS");
//        }
//
//        var id = player.id();
//        return ImmutableGame.builder()
//                .from(this)
//                .putPlayers(id, player)
//                .addPlayerOrder(id)
//                .build();
//    }
//
//    public Game shuffleDeck() {
//        var deck = deck().shuffle();
//        return ImmutableGame.copyOf(this).withDeck(deck);
//    }
//
//    public Game dealStartingHands() {
//        @Var var deck = deck();
//        var players = new HashMap<>(players());
//
//        for (int i = 0; i < Hand.HAND_SIZE; i++) {
//            for (var id : playerOrder()) {
//                var pair = deck.deal();
//                var card = pair.a().orElseThrow();
//                deck = pair.b();
//
//                var player = players.get(id).giveCard(card);
//                players.put(id, player);
//            }
//        }
//
//        return ImmutableGame.builder()
//                .from(this)
//                .deck(deck)
//                .players(players)
//                .build();
//    }
//
//    public Game dealTableCard() {
//        var pair = deck().deal();
//        var card = pair.a().orElseThrow();
//        var deck = pair.b();
//
//        return ImmutableGame.builder()
//                .from(this)
//                .addTableCards(card)
//                .deck(deck)
//                .build();
//    }
//
//    public Game start() {
//        var game = shuffleDeck()
//                .dealStartingHands()
//                .dealTableCard();
//
//        return ImmutableGame.copyOf(game)
//                .withState(State.UncoverTwo);
//    }
//
//    public Game uncoverTwo(Long playerId, int handIndex) {
//        Predicate<Player> stillUncovering = p -> p.uncoveredCardCount() < 2;
//        @Var var player = players().get(playerId);
//
//        if (stillUncovering.test(player)) {
//            player = player.uncoverCard(handIndex);
//        }
//
//        var players = updatePlayers(player);
//        var allReady = players.values().stream()
//                .noneMatch(stillUncovering);
//
//        var state = allReady ? State.Take : state();
//        var turn = allReady ? turn() + 1 : turn();
//
//        return ImmutableGame.builder()
//                .from(this)
//                .players(players)
//                .state(state)
//                .turn(turn)
//                .build();
//    }
//
//    public Game uncover(Long playerId, int handIndex) {
//        var player = players().get(playerId)
//                .uncoverCard(handIndex);
//
//        var players = updatePlayers(player);
//        return ImmutableGame.builder()
//                .from(this)
//                .players(players)
//                .turn(turn() + 1)
//                .build();
//    }
//
//    public Game takeFromDeck(Long playerId) {
//        var pair = deck().deal();
//        var card = pair.a().orElseThrow();
//        var deck = pair.b();
//
//        var player = players().get(playerId).holdCard(card);
//        var players = updatePlayers(player);
//
//        return ImmutableGame.builder()
//                .from(this)
//                .deck(deck)
//                .players(players)
//                .state(State.Discard)
//                .turn(turn() + 1)
//                .build();
//    }
//
//    public Game takeFromTable(Long playerId) {
//        var tableCards = new ArrayDeque<>(tableCards());
//        var card = tableCards.pop();
//        var player = players().get(playerId).holdCard(card);
//        var players = updatePlayers(player);
//
//        return ImmutableGame.builder()
//                .from(this)
//                .tableCards(tableCards)
//                .players(players)
//                .state(State.Discard)
//                .turn(turn() + 1)
//                .build();
//    }
//
//    public Game discardHeldCard(Long playerId) {
//        @Var var player = players().get(playerId);
//        var pair = player.discardHeldCard();
//        var card = pair.a().orElseThrow();
//        player = pair.b();
//
//        var tableCards = new ArrayDeque<>(tableCards());
//        tableCards.push(card);
//
//        var players = updatePlayers(player);
//
//        return ImmutableGame.builder()
//                .from(this)
//                .players(players)
//                .tableCards(tableCards)
//                .build();
//    }
//
//    public Game swapCard(Long playerId, int handIndex) {
//        var pair = players().get(playerId)
//                .swapCard(handIndex);
//
//        var card = pair.a().orElseThrow();
//        var player = pair.b();
//
//        var tableCards = new ArrayDeque<>(tableCards());
//        tableCards.push(card);
//
//        var players = updatePlayers(player);
//        return ImmutableGame.builder()
//                .from(this)
//                .players(players)
//                .tableCards(tableCards)
//                .build();
//    }
//
//
//    public Game handleEvent(GameEvent event) {
//        if (!playerCanAct(event.playerId())) {
//            return this;
//        }
//
//        switch (state()) {
//            case UncoverTwo -> {
//                if (event instanceof GameEvent.Uncover e) {
//                    return uncoverTwo(e.playerId(), e.handIndex());
//                }
//            }
//            case Uncover -> {
//                if (event instanceof GameEvent.Uncover e) {
//                    return uncover(e.playerId(), e.handIndex());
//                }
//            }
//            case Take -> {
//                if (event instanceof GameEvent.TakeFromDeck e) {
//                    return takeFromDeck(e.playerId());
//                } else if (event instanceof GameEvent.TakeFromTable e) {
//                    return takeFromTable(e.playerId());
//                }
//            }
//        }
//
//        throw new IllegalStateException(
//                "Unexpected value. state: " + state() + " event: " + event);
//    }
//
//    public List<Long> playerOrderFrom(Long playerId) {
//        var order = new ArrayList<>(playerOrder());
//        var index = Lib.findIndex(order, playerId).orElseThrow();
//        Collections.rotate(order, -index);
//        return order;
//    }
//
//    /**
//     * Returns the id of the player whose turn it is.
//     */
//    public Long playerTurn() {
//        var index = turn() % players().size();
//        return playerOrder().get(index);
//    }
//
//    public boolean playerCanAct(Long playerId) {
//        return playerTurn().equals(playerId)
//                || state() == State.UncoverTwo;
//    }
//
//    public List<Card.Location> playableCards(Long playerId) {
//        if (!playerCanAct(playerId)) {
//            return List.of();
//        }
//
//        return switch (state()) {
//            case UncoverTwo, Uncover -> List.of(
//                    Card.Location.Hand0,
//                    Card.Location.Hand1,
//                    Card.Location.Hand2,
//                    Card.Location.Hand3,
//                    Card.Location.Hand4,
//                    Card.Location.Hand5);
//
//            case Take -> List.of(Card.Location.Deck, Card.Location.Table);
//
//            case Discard -> List.of(
//                    Card.Location.Held,
//                    Card.Location.Hand0,
//                    Card.Location.Hand1,
//                    Card.Location.Hand2,
//                    Card.Location.Hand3,
//                    Card.Location.Hand4,
//                    Card.Location.Hand5);
//
//            default -> List.of();
//        };
//    }
//
//    private ImmutableMap<Long, Player> updatePlayers(Player player) {
//        var id = player.id();
//        return Lib.updateMap(players(), Map.of(id, player));
//    }
//}
