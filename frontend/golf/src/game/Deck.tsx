import {Card} from "./Card";
import {GameState} from "../types";
import {deckCoords} from "./coords";

interface DeckProps {
  gameState: GameState;
}

export function Deck(props: DeckProps) {
  const {gameState} = props;
  const {x, y} = deckCoords(gameState);

  return (
    <Card
      className="Deck"
      name="2B"
      x={x}
      y={y}
    />
  );
}