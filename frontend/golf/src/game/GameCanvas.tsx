import {Game, HandPosition} from "../types";
import {Deck} from "./Deck";
import {TableCards} from "./TableCards";
import {Hands} from "./Hands";

interface GameCanvasProps {
  userId: number;
  game: Game;
}

export function GameCanvas(props: GameCanvasProps) {
  const width = 600;
  const height = 500;
  const viewBox = `${-width/2} ${-height/2} ${width} ${height}`;

  // const [hoverPos, setHoverPos] = useState<HandPosition | null>();

  const {userId, game} = props;
  if (game == null) return null;

  const {id: gameId, state, tableCards, players, playerTurn} = game;
  const hasStarted = state !== 'INIT';

  return (
    <svg
      className="GameCanvas"
      width={width}
      height={height}
      viewBox={viewBox}
    >
      <Deck userId={userId} gameId={gameId} state={state} playerTurn={playerTurn} />
      {hasStarted &&
        <>
          <TableCards tableCards={tableCards} />
          <Hands
            userId={userId}
            gameId={gameId}
            width={width}
            height={height}
            players={players}
            state={state}
            playerTurn={playerTurn}
          />
        </>
      }
    </svg>
  );
}