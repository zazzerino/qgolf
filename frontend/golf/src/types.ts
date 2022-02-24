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
  playerTurn: number;
  tableCards: CardName[];
  playableCards: CardLocation[];
  players: Player[];
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

export type MessageType = 'UpdateName' | 'CreateGame' | 'StartGame' | 'Event' | 'JoinGame' | 'Chat';

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
