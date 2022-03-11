import {CardName, GameState, HandPosition} from "../types";
import {handCoord} from "./coord";
import {Card, CARD_HEIGHT, CARD_WIDTH} from "./Card";
import {sendUncover} from "../websocket";

export const HAND_PADDING = 2;

function handTransform(width: number, height: number, pos: HandPosition) {
  const {x, y, rotate} = handCoord(width, height, pos);
  return `translate(${x}, ${y}), rotate(${rotate})`;
}

interface HandProps {
  width: number;
  height: number;
  userId: number;
  gameId: number;
  playerId: number;
  position: HandPosition;
  cards: CardName[];
  uncovered: number[];
  state: GameState;
  playerTurn: number;
}

export function Hand(props: HandProps) {
  const {width, height, userId, gameId, playerId, position, cards, uncovered, state, playerTurn} = props;
  const transform = handTransform(width, height, position);

  // the rect is needed to capture mouse events in between the cards
  const rectWidth = (CARD_WIDTH * 3) + (HAND_PADDING * 2);
  const rectHeight = (CARD_HEIGHT * 2) + HAND_PADDING;
  const rectX = (-rectWidth / 2) + 2;
  const rectY = (-rectHeight / 2) + 2;

  return (
    <g
      className="Hand"
      transform={transform}
    >
      <rect x={rectX} y={rectY} width={rectWidth} height={rectHeight} />
      {cards.map((card, key) => {
        const props = {key, userId, gameId, playerId, position, uncovered, card, state, playerTurn};
        return HandCard(props);
      })}
    </g>
  );
}

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

function HandCard(props: HandCardProps) {
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

  const isUsersTurn = userId === playerTurn;
  const isUncoverTwoState = state === "UNCOVER_TWO"
  const isUncoverState = state === "UNCOVER";

  if (isUncoverTwoState || (isUsersTurn && isUncoverState)) {
    sendUncover(userId, gameId, key);
  }
}
