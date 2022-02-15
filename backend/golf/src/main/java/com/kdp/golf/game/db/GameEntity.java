package com.kdp.golf.game.db;

import com.kdp.golf.game.model.*;

import javax.persistence.CascadeType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Entity(name = "Game")
@Table(name = "game")
public class GameEntity {

    @Id
    @SequenceGenerator(name = "gameSeq", sequenceName = "game_id_seq")
    @GeneratedValue(generator = "gameSeq")
    public Long id;
    public Long hostId;
    public String state;
    public int turn;
    public @ElementCollection List<String> deck;
    public @ElementCollection List<String> tableCards;
    public @ElementCollection List<Long> playerOrder;
    public boolean isFinalTurn;
    @OneToMany(
            mappedBy = "game",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    public List<PlayerEntity> players;

    public GameEntity() {}

    public GameEntity(Long id,
                      Long hostId,
                      String state,
                      int turn,
                      List<String> deck,
                      List<String> tableCards,
                      List<Long> playerOrder,
                      boolean isFinalTurn) {
        this.id = id;
        this.hostId = hostId;
        this.state = state;
        this.turn = turn;
        this.deck = new ArrayList<>(deck);
        this.tableCards = new ArrayList<>(tableCards);
        this.playerOrder = new ArrayList<>(playerOrder);
        this.isFinalTurn = isFinalTurn;
    }

    public static GameEntity from(Game g) {
        var state = g.state().toString();
        var deck = Card.mapName(g.deck().cards());
        var tableCards = Card.mapName(g.tableCards());

        var entity = new GameEntity(
                g.id(),
                g.hostId(),
                state,
                g.turn(),
                deck,
                tableCards,
                g.playerOrder(),
                g.isFinalTurn());

        entity.players = g.players().stream()
                .map(p -> PlayerEntity.from(p, entity))
                .collect(Collectors.toCollection(ArrayList::new));

        return entity;
    }

    public Game toGame() {
        if (players.isEmpty()) {
            throw new IllegalStateException("players need to be set before toGame() is called");
        }

        var state = GameState.valueOf(this.state);
        var deckCards = Card.mapFrom(deck);
        var deck = new Deck(deckCards);
        var tableCards = Card.mapFrom(this.tableCards);

        var players = this.players.stream()
                .map(PlayerEntity::toPlayer)
                .collect(Collectors.toMap(
                        Player::id,
                        Function.identity()));

        return new Game(
                id,
                hostId,
                state,
                turn,
                deck,
                tableCards,
                players,
                playerOrder,
                isFinalTurn);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GameEntity that = (GameEntity) o;
        return turn == that.turn
                && isFinalTurn == that.isFinalTurn
                && id.equals(that.id)
                && hostId.equals(that.hostId)
                && state.equals(that.state)
                && deck.equals(that.deck)
                && tableCards.equals(that.tableCards)
                && playerOrder.equals(that.playerOrder)
                && Objects.equals(players, that.players);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, hostId, state, turn, deck, tableCards, playerOrder, isFinalTurn, players);
    }

    @Override
    public String toString() {
        return "GameEntity{" +
                "id=" + id +
                ", hostId=" + hostId +
                ", state='" + state + '\'' +
                ", turn=" + turn +
                ", deck=" + deck +
                ", tableCards=" + tableCards +
                ", playerOrder=" + playerOrder +
                ", isFinalTurn=" + isFinalTurn +
                '}';
    }
}
