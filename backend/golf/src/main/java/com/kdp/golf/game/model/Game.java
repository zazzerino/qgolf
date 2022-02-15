package com.kdp.golf.game.model;

import java.util.ArrayList;
import java.util.Collection;
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
    private boolean isFinalTurn;

    public final static int DECK_COUNT = 2;

    public Game(Long id,
                Long hostId,
                GameState state,
                int turn,
                Deck deck,
                List<Card> tableCards,
                Map<Long, Player> players,
                List<Long> playerOrder,
                boolean isFinalTurn) {
        this.id = id;
        this.hostId = hostId;
        this.state = state;
        this.turn = turn;
        this.deck = deck;
        this.tableCards = new ArrayList<>(tableCards);
        this.players = new HashMap<>(players);
        this.playerOrder = new ArrayList<>(playerOrder);
        this.isFinalTurn = isFinalTurn;
    }

    public static Game create(Long id, Player host) {
        var state = GameState.Init;
        var turn = 0;
        var deck = Deck.create(DECK_COUNT);
        var tableCards = List.<Card>of();
        var players = Map.of(host.id(), host);
        var playerOrder = List.of(host.id());
        var isFinalTurn = false;

        return new Game(
                id,
                host.id(),
                state,
                turn,
                deck,
                tableCards,
                players,
                playerOrder,
                isFinalTurn);
    }

    public void addPlayer(Player p) {
        if (players.values().size() >= 4) {
            throw new IllegalStateException("can only have a maximum of four players");
        }

        players.put(p.id(), p);
        playerOrder.add(p.id());
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
        setState(GameState.UncoverTwo);
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
            setState(GameState.Take);
            turn++;
        }
    }

    public void uncover(Long playerId, int handIndex) {
        var player = players.get(playerId);
        player.uncoverCard(handIndex);
        setState(GameState.Take);
        turn++;
    }

    public void takeFromDeck(Long playerId) {
        var card = deck.deal().orElseThrow();
        var player = players.get(playerId);
        player.holdCard(card);
        setState(GameState.Discard);
        turn++;
    }

    public void takeFromTable(Long playerId) {
        var card = tableCards.remove(0);
        var player = players.get(playerId);
        player.holdCard(card);
        setState(GameState.Discard);
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
            case Take -> {
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

    public List<Card.Location> playableCards(Long playerId) {
        if (!isPlayersTurn(playerId)) {
            return List.of();
        }

        return switch (state) {
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

    public Long id() { return id; }

    public Long hostId() { return hostId; }

    public void setHostId(Long hostId) {
        this.hostId = hostId;
    }

    public GameState state() { return state; }

    public void setState(GameState state) {
        this.state = state;
    }

    public int turn() { return turn; }

    public Deck deck() { return deck; }

    public List<Card> tableCards() { return tableCards; }

    public Collection<Player> players() {
        return players.values();
    }

    public List<Long> playerOrder() { return playerOrder; }

    public boolean isFinalTurn() { return isFinalTurn; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Game game = (Game) o;
        return turn == game.turn
                && isFinalTurn == game.isFinalTurn
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
        return Objects.hash(id, hostId, state, turn, deck, tableCards, players, playerOrder, isFinalTurn);
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
                ", isFinalTurn=" + isFinalTurn +
                '}';
    }
}
