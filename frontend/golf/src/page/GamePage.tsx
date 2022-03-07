import {Game, User} from "../types";
import {sendCreateGameMessage} from "../websocket";

export function GamePage(props: {user: User; game?: Game}) {
  const {user, game} = props;

  return (
    <div className="GamePage">
      <h2>Game</h2>
      <CreateGameButton userId={user.id} />
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
