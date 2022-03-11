import {Dispatch} from "react";
import {
  Action,
  CreateGameMessage,
  GameResponse,
  Message, Response,
  StartGameMessage, TakeFromDeckMessage, UncoverMessage,
  UpdateNameMessage,
  UserResponse
} from "./types";

const WS_URL = "ws://localhost:8080/ws";

let SOCKET: WebSocket;
let DISPATCH: Dispatch<Action>;

export function initWebsocket(dispatch: Dispatch<Action>) {
  SOCKET = new WebSocket(WS_URL);
  SOCKET.onopen = onOpen;
  SOCKET.onclose = onClose;
  SOCKET.onerror = onError;
  SOCKET.onmessage = onMessage;

  DISPATCH = dispatch;
}

function onOpen() {
  console.log("websocket connection established");
}

function onClose() {
  console.log("websocket connection closed");
}

function onError(event: Event) {
  console.error("WEBSOCKET ERROR: " + JSON.stringify(event));
}

function onMessage(event: MessageEvent) {
  console.log("message received ↓");
  console.log(event.data);

  const response = JSON.parse(event.data) as Response;

  switch (response.type) {
    case "User": return handleUserResponse(response as UserResponse);
    case "Game": return handleGameResponse(response as GameResponse);
  }
}

function send(message: Message) {
  SOCKET.send(JSON.stringify(message));
}

export function sendUpdateNameMessage(userId: number, name: string) {
  const message: UpdateNameMessage = {
    type: "UpdateName",
    userId,
    name,
  };

  send(message);
}

export function sendCreateGame(userId: number) {
  const message: CreateGameMessage = {
    type: "CreateGame",
    userId,
  };

  send(message);
}

export function sendStartGame(userId: number, gameId: number) {
  const message: StartGameMessage = {
    type: "StartGame",
    userId,
    gameId,
  };

  send(message);
}

export function sendUncover(userId: number, gameId: number, handIndex: number) {
  const message: UncoverMessage = {
    type: "Uncover",
    userId,
    gameId,
    handIndex,
  }

  send(message);
}

export function sendTakeFromDeck(userId: number, gameId: number) {
  const message: TakeFromDeckMessage = {
    type: "TakeFromDeck",
    userId,
    gameId
  }

  send(message);
}

function handleUserResponse(response: UserResponse) {
  const user = response.user;
  const action: Action = {type: "setUser", user};
  DISPATCH(action);
}

function handleGameResponse(response: GameResponse) {
  const game = response.game;
  const action: Action = {type: "setGame", game};
  DISPATCH(action);
}
