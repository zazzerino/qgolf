import {CardName, GameState, HandPosition} from "../types";
import {Card, CARD_HEIGHT, CARD_WIDTH} from "./Card";
import {HAND_PADDING} from "./Hand";
import {sendUncover} from "../websocket";

interface HandCardProps {
  key: number;
  userId: number;
  gameId: number;
  playerId: number;
  position: HandPosition;
  uncovered: number[];
  card: CardName;
  state: GameState;
  playerTurn: number;
}

export function HandCard(props: HandCardProps) {
  const {key, userId, gameId, playerId, position, uncovered, card, state, playerTurn} = props;
  const className = `${position}-HAND_${key}`; // e.g. 'BOTTOM-HAND_0' (the 1st card in the bottom hand)
  const name = uncovered.includes(key) ? card : "2B";
  const xOffset = key % 3; // the x coords of cards 0-2 are the same as cards 3-5
  const x = (CARD_WIDTH * xOffset) + (HAND_PADDING * xOffset) - CARD_WIDTH;
  const y = (key < 3 ? 0 : CARD_HEIGHT + HAND_PADDING) - (CARD_HEIGHT / 2);
  const onClick = () => onHandCardClick(userId, gameId, playerId, state, playerTurn, key);

  return (
    <Card
      key={key}
      className={className}
      cardName={name}
      x={x}
      y={y}
      onClick={onClick}
    />
  );
}

function onHandCardClick(userId: number,
                         gameId: number,
                         playerId: number,
                         state: GameState,
                         playerTurn: number,
                         key: number) {
  const isUsersCard = userId === playerId;
  if (!isUsersCard) return;

  const isUncoverTwoState = state === "UNCOVER_TWO"
  const isUsersTurn = userId === playerTurn;
  const isUncoverState = state === "UNCOVER";
  const canUncover = isUsersTurn && isUncoverState;

  if (isUncoverTwoState || canUncover) {
    sendUncover(userId, gameId, key);
  }
}
