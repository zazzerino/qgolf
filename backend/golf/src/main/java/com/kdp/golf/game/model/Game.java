package com.kdp.golf.game.model;

import com.google.common.collect.Iterables;
import com.kdp.golf.lib.Lib;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;
import java.util.*;

public class Game {

    private @Nullable Long id; // the id will be created by the database, so the initial object will have a null id
    private Long hostId;
    private State state;
    private int turn;
    private boolean finalTurn;
    private final Deck deck;
    private final Deque<Card> tableCards;
    private final LinkedHashMap<Long, Player> players;

    public static final int DECK_COUNT = 2;
    public static final int MAX_PLAYERS = 4;

    public enum State {
        INIT,
        UNCOVER_TWO,
        TAKE,
        DISCARD,
        UNCOVER,
        GAME_OVER,
    }

    public Game(@Nullable Long id,
                Long hostId,
                State state,
                int turn,
                boolean finalTurn,
                Deck deck,
                Collection<Card> tableCards,
                Map<Long, Player> players) {
        this.id = id;
        this.hostId = hostId;
        this.state = state;
        this.turn = turn;
        this.finalTurn = finalTurn;
        this.deck = deck;
        this.tableCards = new ArrayDeque<>(tableCards);
        this.players = new LinkedHashMap<>(players);
    }

    public static Game create(Long id, Player host) {
        var state = State.INIT;
        var turn = 0;
        var finalTurn = false;
        var deck = Deck.create(DECK_COUNT);
        var tableCards = Collections.<Card>emptyList();

        var players = new LinkedHashMap<Long, Player>();
        players.put(host.id(), host);

        return new Game(id, host.id(), state, turn, finalTurn, deck, tableCards, players);
    }

    public void addPlayer(Player p) {
        if (players.size() >= MAX_PLAYERS) {
            throw new IllegalStateException("attempt to add more than MAX_PLAYERS");
        }

        players.put(p.id(), p);
    }

    public void removePlayer(Player p) {
        players.remove(p.id());

        // if the leaving player is the host, make the next player the host
        if (Objects.equals(hostId, p.id())) {
            hostId = players.keySet().stream().findFirst().orElseThrow();
        }
    }

    private void shuffleDeck() {
        deck.shuffle();
    }

    private void dealStartingHands() {
        for (var i = 0; i < Hand.HAND_SIZE; i++) {
            for (var player : players.values()) {
                var card = deck.deal().orElseThrow();
                player.giveCard(card);
            }
        }
    }

    private void dealTableCard() {
        var card = deck.deal().orElseThrow();
        tableCards.push(card);
    }

    public void start() {
        shuffleDeck();
        dealStartingHands();
        dealTableCard();
        state = State.UNCOVER_TWO;
    }

    public void handleEvent(GameEvent event) {
        var player = players.get(event.playerId());
        if (!playerCanAct(player)) return;

        switch (state) {
            case UNCOVER_TWO -> {
                if (event instanceof GameEvent.Uncover e) {
                    uncoverTwo(player, e.handIndex());
                }
            }
            case UNCOVER -> {
                if (event instanceof GameEvent.Uncover e) {
                    uncover(player, e.handIndex());
                }
            }
            case TAKE -> {
                if (event instanceof GameEvent.TakeFromDeck) {
                    takeFromDeck(player);
                } else if (event instanceof GameEvent.TakeFromTable) {
                    takeFromTable(player);
                }
            }
        }

        throw new IllegalStateException(
                "unexpected value. state: " + state() + " event: " + event);
    }

    private void uncoverTwo(Player p, int handIndex) {
        if (p.stillUncoveringTwo()) {
            p.uncoverCard(handIndex);
        } else return;

        var allReady = players.values()
                .stream()
                .noneMatch(Player::stillUncoveringTwo);

        if (allReady) {
            state = State.TAKE;
            ++turn;
        }
    }

    private void takeFromDeck(Player p) {
        var card = deck.deal().orElseThrow();
        p.holdCard(card);
        state = State.DISCARD;
    }

    private void takeFromTable(Player p) {
        var card = tableCards.pop();
        p.holdCard(card);
        state = State.DISCARD;
    }

    private void discard(Player p) {
        var card = p.discard();
        tableCards.push(card);
    }

    private void uncover(Player p, int handIndex) {
        p.uncoverCard(handIndex);
        ++turn;
        nextPlayer();
    }

    private void swapCard(Player p, int handIndex) {
        var card = p.swapCard(handIndex);
        tableCards.push(card);
        ++turn;
        nextPlayer();
    }

    private boolean playerCanAct(Player p) {
        return playerTurn().equals(p.id())
                || (state == State.UNCOVER_TWO && p.stillUncoveringTwo());
    }

    /**
     * @return the player ids starting from `playerId` clockwise around the table
     */
    public List<Long> playerOrderFrom(Long playerId) {
        var playerIds = new ArrayList<>(players.keySet());
        var index = Lib.findIndex(playerIds, playerId).orElseThrow();
        Collections.rotate(playerIds, -index);
        return playerIds;
    }

    /**
     * @return the card locations that player `p` can interact with
     */
    public List<CardLocation> playableCards(Player p) {
        if (!playerCanAct(p)) return List.of();

        return switch (state) {
            case UNCOVER_TWO, UNCOVER -> CardLocation.UNCOVER_LOCATIONS;
            case TAKE -> CardLocation.TAKE_LOCATIONS;
            case DISCARD -> CardLocation.DISCARD_LOCATIONS;
            default -> Collections.emptyList();
        };
    }

    /**
     * @return the id of the current player
     */
    public Long playerTurn() {
        return players.values()
                .stream()
                .findFirst()
                .map(Player::id)
                .orElseThrow();
    }

    private void nextPlayer() {
        Lib.cycleLinkedHashMap(players);
    }

    public Long id() { return id; }

    public void setId(@NotNull Long id) { this.id = id; }

    public Long hostId() { return hostId; }

    public State state() { return state; }

    public int turn() { return turn; }

    public Deck deck() { return deck; }

    public Collection<Card> tableCards() { return tableCards; }

    public boolean isFinalTurn() { return finalTurn; }

    public Optional<Player> getPlayer(Long playerId) {
        return Optional.ofNullable(players.get(playerId));
    }

    public Collection<Player> players() {
        return players.values();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Game g = (Game) o;
        return turn == g.turn
                && finalTurn == g.finalTurn
                && Objects.equals(id, g.id)
                && hostId.equals(g.hostId)
                && state == g.state
                && deck.equals(g.deck)
                && Iterables.elementsEqual(tableCards, g.tableCards)
                && players.equals(g.players);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, hostId, state, turn, finalTurn, deck, tableCards, players);
    }

    @Override
    public String toString() {
        return "Game{" +
                "id=" + id +
                ", hostId=" + hostId +
                ", state=" + state +
                ", turn=" + turn +
                ", finalTurn=" + finalTurn +
                ", deck=" + deck +
                ", tableCards=" + tableCards +
                ", players=" + players +
                '}';
    }
}
