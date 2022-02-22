export interface Point {
  x: number;
  y: number;
}

export interface User {
  id: number;
  name: string;
}

export type GameState =
  | 'Init'
  | 'UncoverTwo'
  | 'Take'
  | 'Discard'
  | 'Uncover'
  | 'FinalTake'
  | 'FinalDiscard'
  | 'GameOver'
  ;

export type CardName = string;

export type CardLocation =
  | 'Deck'
  | 'Table'
  | 'Held'
  | 'Hand0'
  | 'Hand1'
  | 'Hand2'
  | 'Hand3'
  | 'Hand4'
  | 'Hand5'
  ;

export type HandPosition = 'Bottom' | 'Left' | 'Top' | 'Right';

export interface Player {
  id: number;
  name: string;
  handPosition: HandPosition;
  cards: CardName[];
  uncoveredCards: number[];
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
