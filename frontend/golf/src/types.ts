export interface Point {
  x: number;
  y: number;
}

export interface User {
  id: number;
  name: string;
}

export type GameState =
  | 'INIT'
  | 'UNCOVER_TWO'
  | 'TAKE'
  | 'DISCARD'
  | 'UNCOVER'
  | 'GAME_OVER'
  ;

/**
 * Must be 2 characters.
 * e.g. "AS" is the ace of spades, "2H" is the two of hearts
 */
export type CardName = string;

export type CardLocation =
  | 'DECK'
  | 'TABLE'
  | 'HELD'
  | 'HAND_0'
  | 'HAND_1'
  | 'HAND_2'
  | 'HAND_3'
  | 'HAND_4'
  | 'HAND_5'
  ;

export type HandPosition = 'BOTTOM' | 'LEFT' | 'TOP' | 'RIGHT';

export interface Player {
  id: number;
  name: string;
  handPosition: HandPosition;
  cards: CardName[];
  uncovered: number[];
  heldCard?: CardName;
  score: number;
}

export interface Game {
  id: number;
  userId: number;
  hostId: number;
  state: GameState;
  turn: number;
  finalTurn: boolean;
  playerTurn: number;
  tableCards: CardName[];
  playableCards: CardLocation[];
  players: Player[];
}

export type GameEventType =
  | 'UNCOVER'
  | 'TAKE_FROM_DECK'
  | 'TAKE_FROM_TABLE'
  | 'SWAP_CARD'
  | 'DISCARD'
  ;

export interface GameEvent {
  type: GameEventType;
  gameId: number;
  playerId: number;
}

export interface UncoverEvent extends GameEvent {
  type: 'UNCOVER';
  handIndex: number;
}

export interface TakeFromDeckEvent extends GameEvent {
  type: 'TAKE_FROM_DECK';
}

export interface TakeFromTableEvent extends GameEvent {
  type: 'TAKE_FROM_TABLE';
}

export interface SwapCardEvent extends GameEvent {
  type: 'SWAP_CARD';
  handIndex: number;
}

export interface DiscardEvent extends GameEvent {
  type: 'DISCARD';
}

export interface AppState {
  user: User;
  games: Game[];
  game?: Game;
}

export type Action =
  | {type: 'setUser', user: User}
  | {type: 'setGames', games: Game[]}
  | {type: 'setGame', game: Game}
  ;

export type MessageType =
  | 'UpdateName'
  | 'CreateGame'
  | 'StartGame'
  | 'JoinGame'
  | 'GameEvent'
  | 'Chat'
  ;

export interface Message {
  type: MessageType;
  userId: number;
}

export interface UpdateNameMessage extends Message {
  type: 'UpdateName';
  name: string;
}

export interface CreateGameMessage extends Message {
  type: 'CreateGame';
}

export interface StartGameMessage extends Message {
  type: 'StartGame';
  gameId: number;
}

export interface GameEventMessage extends Message {
  type: 'GameEvent';
  gameId: number;
  eventType: GameEventType;
  handIndex?: number;
}

export type ResponseType = 'User' | 'Game' | 'Games';

export interface Response {
  type: ResponseType;
}

export interface UserResponse extends Response {
  type: 'User';
  user: User;
}

export interface GameResponse extends Response {
  type: 'Game';
  game: Game;
}

export interface GamesResponse extends Response {
  type: 'Games';
  games: Game[];
}
