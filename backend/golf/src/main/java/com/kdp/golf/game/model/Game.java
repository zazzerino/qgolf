package com.kdp.golf.game.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.kdp.golf.game.model.card.Card;
import com.kdp.golf.game.model.card.CardLocation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Predicate;

public class Game {

    private final Long id;
    private Long hostId;
    private GameState state;
    private int turn;
    private final Deck deck;
    private final List<Card> tableCards;
    private final Map<Long, Player> players;
    private final List<Long> playerOrder;

    public final static int DECK_COUNT = 2;

    public Game(Long id,
                Long hostId,
                GameState state,
                int turn,
                Deck deck,
                List<Card> tableCards,
                Map<Long, Player> players,
                List<Long> playerOrder) {
        this.id = id;
        this.hostId = hostId;
        this.state = state;
        this.turn = turn;
        this.deck = deck;
        this.tableCards = new ArrayList<>(tableCards);
        this.players = new HashMap<>(players);
        this.playerOrder = new ArrayList<>(playerOrder);
    }

    public static Game create(Long id, Player host) {
        var state = GameState.Init;
        var turn = 0;
        var deck = Deck.create(DECK_COUNT);
        var tableCards = List.<Card>of();
        var players = Map.of(host.id(), host);
        var playerOrder = List.of(host.id());

        return new Game(id, host.id(), state, turn, deck, tableCards, players, playerOrder);
    }

    public void addPlayer(Player player) {
        if (players.values().size() >= 4) {
            throw new IllegalStateException("can only have a maximum of four players");
        }

        players.put(player.id(), player);
        playerOrder.add(player.id());
    }

    public void shuffleDeck() {
        deck.shuffle();
    }

    public void dealStartingHands() {
        for (int i = 0; i < Hand.HAND_SIZE; i++) {
            for (var id : playerOrder) {
                var card = deck.deal().orElseThrow();
                var player = players.get(id);
                player.giveCard(card);
            }
        }
    }

    public void dealTableCard() {
        var card = deck.deal().orElseThrow();
        tableCards.add(card);
    }

    public void start() {
        shuffleDeck();
        dealStartingHands();
        dealTableCard();
        state = GameState.UncoverTwo;
    }

    public void uncoverTwo(Long playerId, int handIndex) {
        Predicate<Player> stillUncovering = p -> p.uncoveredCount() < 2;
        var player = players.get(playerId);

        if (stillUncovering.test(player)) {
            player.uncoverCard(handIndex);
        }

        var allReady = players.values()
                .stream()
                .allMatch(stillUncovering);

        if (allReady) {
            state = GameState.Take;
            turn++;
        }
    }

    public void uncover(Long playerId, int handIndex) {
        var player = players.get(playerId);
        player.uncoverCard(handIndex);
        state = GameState.Take;
        turn++;
    }

    public void takeFromDeck(Long playerId) {
        var card = deck.deal().orElseThrow();
        var player = players.get(playerId);
        player.holdCard(card);

        state = state == GameState.FinalTake
                ? GameState.FinalDiscard
                : GameState.Discard;

        turn++;
    }

    public void takeFromTable(Long playerId) {
        var card = tableCards.remove(0);
        var player = players.get(playerId);
        player.holdCard(card);

        state = state == GameState.FinalTake
                ? GameState.FinalDiscard
                : GameState.Discard;

        turn++;
    }

    public void discard(Long playerId) {
        var player = players.get(playerId);
        var card = player.discard();
        tableCards.add(card);
    }

    public void swapCard(Long playerId, int handIndex) {
        var player = players.get(playerId);
        var card = player.swapCard(handIndex);
        tableCards.add(card);
    }

    public void handleEvent(GameEvent event) {
        if (!isPlayersTurn(event.playerId())) {
            return;
        }

        switch (state) {
            case UncoverTwo -> {
                if (event instanceof GameEvent.Uncover e) {
                    uncoverTwo(e.playerId(), e.handIndex());
                }
            }
            case Uncover -> {
                if (event instanceof GameEvent.Uncover e) {
                    uncover(e.playerId(), e.handIndex());
                }
            }
            case Take, FinalTake -> {
                if (event instanceof GameEvent.TakeFromDeck e) {
                    takeFromDeck(e.playerId());
                } else if (event instanceof GameEvent.TakeFromTable e) {
                    takeFromTable(e.playerId());
                }
            }
        }
    }

    public List<Long> orderFrom(Long playerId) {
        var index = playerOrder.indexOf(playerId);

        if (index < 0) {
            throw new NoSuchElementException("player with id " + playerId + " not found");
        }

        var order = new ArrayList<>(playerOrder);
        Collections.rotate(order, -index);
        return order;
    }

    /**
     * Returns the id of the player whose turn it is.
     */
    public Long playerTurn() {
        var index = turn % players.size();
        return playerOrder.get(index);
    }

    public boolean isPlayersTurn(Long playerId) {
        return state == GameState.UncoverTwo || playerTurn().equals(playerId);
    }

    public List<CardLocation> playableCards(Long playerId) {
        if (!isPlayersTurn(playerId)) {
            return List.of();
        }

        return switch (state) {
            case UncoverTwo, Uncover -> List.of(
                    CardLocation.Hand0,
                    CardLocation.Hand1,
                    CardLocation.Hand2,
                    CardLocation.Hand3,
                    CardLocation.Hand4,
                    CardLocation.Hand5);

            case Take, FinalTake -> List.of(CardLocation.Deck, CardLocation.Table);

            case Discard, FinalDiscard -> List.of(
                    CardLocation.Held,
                    CardLocation.Hand0,
                    CardLocation.Hand1,
                    CardLocation.Hand2,
                    CardLocation.Hand3,
                    CardLocation.Hand4,
                    CardLocation.Hand5);

            default -> List.of();
        };
    }

    @JsonProperty
    public Long id() {
        return id;
    }

    @JsonProperty
    public Long hostId() {
        return hostId;
    }

    public void setHostId(Long hostId) {
        this.hostId = hostId;
    }

    @JsonProperty
    public GameState state() {
        return state;
    }

    @JsonProperty
    public int turn() {
        return turn;
    }

    @JsonProperty
    public Deck deck() {
        return deck;
    }

    @JsonProperty
    public List<Card> tableCards() {
        return tableCards;
    }

    @JsonProperty
    public Map<Long, Player> players() {
        return players;
    }

    @JsonProperty
    public List<Long> playerOrder() {
        return playerOrder;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Game game = (Game) o;
        return turn == game.turn
                && id.equals(game.id)
                && hostId.equals(game.hostId)
                && state == game.state
                && deck.equals(game.deck)
                && tableCards.equals(game.tableCards)
                && players.equals(game.players)
                && playerOrder.equals(game.playerOrder);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, hostId, state, turn, deck, tableCards, players, playerOrder);
    }

    @Override
    public String toString() {
        return "Game{" +
                "id=" + id +
                ", hostId=" + hostId +
                ", state=" + state +
                ", turn=" + turn +
                ", deck=" + deck +
                ", tableCards=" + tableCards +
                ", players=" + players +
                ", playerOrder=" + playerOrder +
                '}';
    }
}
