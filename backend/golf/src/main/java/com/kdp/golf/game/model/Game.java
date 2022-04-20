package com.kdp.golf.game.model;

import com.google.common.collect.Iterables;
import com.kdp.golf.lib.Lib;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;
import java.util.*;

public class Game {

    // the id will be created by the database, so the initial object will have a null id
    private @Nullable Long id;
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

    public void addPlayer(Player player) {
        if (players.size() >= MAX_PLAYERS) {
            throw new IllegalStateException("attempt to add more than MAX_PLAYERS");
        }

        players.put(player.id(), player);
    }

    public void removePlayer(Player player) {
        players.remove(player.id());

        // if the leaving player is the host, make the 2nd player to join the host
        if (Objects.equals(hostId, player.id())) {
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
    }

    private void uncoverTwo(Player player, int handIndex) {
        if (player.stillUncoveringTwo()) {
            player.uncoverCard(handIndex);
        } else return;

        var allReady = players.values()
                .stream()
                .noneMatch(Player::stillUncoveringTwo);

        if (allReady) {
            state = State.TAKE;
            ++turn;
        }
    }

    private void uncover(Player player, int handIndex) {
        player.uncoverCard(handIndex);
        state = State.TAKE;
        ++turn;
        nextPlayer();
    }

    private void takeFromDeck(Player player) {
        var card = deck.deal().orElseThrow();
        player.holdCard(card);
        state = State.DISCARD;
    }

    private void takeFromTable(Player player) {
        var card = tableCards.pop();
        player.holdCard(card);
        state = State.DISCARD;
    }

    private void discard(Player player) {
        var card = player.discard();
        tableCards.push(card);

        var hasOneCoveredCard = player.uncoveredCardCount() == Hand.HAND_SIZE - 1;

        if (hasOneCoveredCard) {
            state = State.TAKE;
            ++turn;
        } else {
            state = State.UNCOVER;
        }
    }

    private void swapCard(Player player, int handIndex) {
        var card = player.swapCard(handIndex);
        tableCards.push(card);
        ++turn;
        nextPlayer();
    }

    private boolean playerCanAct(Player player) {
        var isPlayersTurn = playerTurn().equals(player.id());
        // When the state is UNCOVER_TWO, any player can act.
        var isUncoverTwo = state == State.UNCOVER_TWO && player.stillUncoveringTwo();
        return isPlayersTurn || isUncoverTwo;
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
     * @return the card locations that `player` can interact with
     */
    public List<CardLocation> playableCards(Player player) {
        if (!playerCanAct(player)) return Collections.emptyList();

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

    public Long id() {
        return id;
    }

    public void setId(@NotNull Long id) {
        this.id = id;
    }

    public Long hostId() {
        return hostId;
    }

    public State state() {
        return state;
    }

    public int turn() {
        return turn;
    }

    public Deck deck() {
        return deck;
    }

    public Collection<Card> tableCards() {
        return tableCards;
    }

    public boolean isFinalTurn() {
        return finalTurn;
    }

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
