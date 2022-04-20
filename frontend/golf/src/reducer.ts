import {Action, AppState, User} from "./types";

export const DEFAULT_USER: User = {id: -1, name: "anonymous"}

export const INITIAL_STATE: AppState = {
  user: DEFAULT_USER,
  games: [],
}

export function rootReducer(state: AppState, action: Action) {
  switch (action.type) {
    case "setUser": return {...state, user: action.user};
    case 'setGames': return {...state, games: action.games};
    case 'setGame': return {...state, game: action.game};
  }
}
