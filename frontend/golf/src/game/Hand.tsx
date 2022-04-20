import {CardName, GameState, HandPosition} from "../types";
import {handCoord} from "./coord";
import {CARD_HEIGHT, CARD_WIDTH} from "./Card";
import {HandCard} from "./HandCard";

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
        return HandCard(
          {key, userId, gameId, playerId, position, uncovered, card, state, playerTurn});
      })}
    </g>
  );
}
