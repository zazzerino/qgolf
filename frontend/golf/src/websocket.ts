import {Dispatch} from "react";
import {
  Action,
  CreateGameMessage,
  GameResponse,
  Message, Response,
  StartGameMessage,
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

export function sendCreateGameMessage(userId: number) {
  const message: CreateGameMessage = {
    type: "CreateGame",
    userId,
  };

  send(message);
}

export function sendStartGameMessage(userId: number, gameId: number) {
  const message: StartGameMessage = {
    type: "StartGame",
    userId,
    gameId,
  };

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
