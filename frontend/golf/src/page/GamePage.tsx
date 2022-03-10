import {Game, User} from "../types";
import {sendCreateGameMessage, sendStartGameMessage} from "../websocket";
import {GameCanvas} from "../game/GameCanvas";

export function GamePage(props: {user: User; game?: Game}) {
  const {user, game} = props;

  return (
    <div className="GamePage">
      <h2>Game</h2>
      {game && <GameCanvas game={game} />}
      <CreateGameButton userId={user.id} />
      {game && <StartGameButton userId={user.id} gameId={game.id} />}
    </div>
  );
}

function CreateGameButton(props: {userId: number}) {
  return (
    <button
      className="CreateGameButton"
      onClick={() => sendCreateGameMessage(props.userId)}
    >
      Create Game
    </button>
  );
}

function StartGameButton(props: {userId: number, gameId: number}) {
  const {userId, gameId} = props;

  return (
    <button
      className="StartGameButton"
      onClick={() => sendStartGameMessage(userId, gameId)}
    >
      Start Game
    </button>
  );
}
