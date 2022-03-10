import {Game} from "../types";
import {Deck} from "./Deck";
import {TableCards} from "./TableCards";

interface GameCanvasProps {
  game: Game;
}

export function GameCanvas(props: GameCanvasProps) {
  const width = 600;
  const height = 500;
  const viewBox = `${-width / 2} ${-height / 2} ${width} ${height}`;

  const {game} = props;
  if (game == null) return null;

  const {state, tableCards} = game;
  const hasStarted = state !== 'INIT';

  return (
    <svg
      className="GameCanvas"
      width={width}
      height={height}
      viewBox={viewBox}
    >
      <Deck gameState={game.state} />
      {hasStarted &&
        <>
          <TableCards tableCards={tableCards} />
        </>
      }
    </svg>
  );
}