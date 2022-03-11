import {GameState, HandPosition, Point} from "../types";
import {CARD_HEIGHT, CARD_WIDTH} from "./Card";
import {HAND_PADDING} from "./Hand";

export function deckCoord(state: GameState): Point {
  const x = state === "INIT" ? 0 : (-CARD_WIDTH / 2) - 2;
  const y = 0;
  return {x, y};
}

export const TABLE_CARD_COORD: Point = {
  x: CARD_WIDTH / 2 + 2,
  y: 0
};

export function handCoord(width: number, height: number, pos: HandPosition) {
  let x: number;
  let y: number;
  let rotate: number;

  switch (pos) {
    case 'BOTTOM':
      x = 0
      y = (height / 2) - CARD_HEIGHT - (HAND_PADDING * 4);
      rotate = 0;
      break;
    case 'LEFT':
      x = -(width / 2) + CARD_HEIGHT + (HAND_PADDING * 4);
      y = 0;
      rotate = 90;
      break;
    case 'TOP':
      x = 0;
      y = -(height / 2) + CARD_HEIGHT + (HAND_PADDING * 4);
      rotate = 180;
      break;
    case 'RIGHT':
      x = width / 2 - CARD_HEIGHT - (HAND_PADDING * 4);
      y = 0;
      rotate = 270;
      break;
  }

  return {x, y, rotate};
}
