import {GameState, Player} from "../types";
import {Hand} from "./Hand";

interface HandsProps {
  width: number;
  height: number;
  userId: number;
  gameId: number;
  players: Player[];
  state: GameState;
  playerTurn: number;
}

export function Hands(props: HandsProps) {
  const {width, height, userId, gameId, players, state, playerTurn} = props;

  return (
    <>
      {players.map((player, key) => {
        const {id: playerId, handPosition, cards, uncovered} = player;
        return (
          <Hand
            key={key}
            userId={userId}
            gameId={gameId}
            playerId={playerId}
            width={width}
            height={height}
            position={handPosition}
            cards={cards}
            uncovered={uncovered}
            state={state}
            playerTurn={playerTurn}
          />
        );
      })}
    </>
  );
}
