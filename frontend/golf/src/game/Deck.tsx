import {Card} from "./Card";
import {GameState} from "../types";
import {deckCoord} from "./coord";
import {sendTakeFromDeck} from "../websocket";

interface DeckProps {
  userId: number;
  gameId: number;
  state: GameState;
  playerTurn: number;
}

export function Deck(props: DeckProps) {
  const {userId, gameId, state, playerTurn} = props;
  const {x, y} = deckCoord(state);
  const onClick = () => onDeckClick(userId, gameId, state, playerTurn);

  return (
    <Card
      className="Deck"
      cardName="2B"
      x={x}
      y={y}
      onClick={onClick}
    />
  );
}

function onDeckClick(userId: number, gameId: number, state: GameState, playerTurn: number) {
  const isUsersTurn = userId === playerTurn;
  const isTakeState = state === "TAKE";
  console.log('clicked deck');

  if (isUsersTurn && isTakeState) {
    sendTakeFromDeck(userId, gameId);
  }
}