import {GameState, Point} from "../types";
import {CARD_WIDTH} from "./Card";

export function deckCoords(gameState: GameState): Point {
  const x = gameState === "INIT" ? 0 : (-CARD_WIDTH / 2) - 2;
  const y = 0;
  return {x, y};
}

export const TABLE_CARD_COORD = {
  x: CARD_WIDTH / 2 + 2,
  y: 0
};
