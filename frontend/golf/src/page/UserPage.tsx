import React, {Dispatch, SetStateAction, useState} from "react";
import {sendUpdateName} from "../websocket";

export function UserPage(props: {userId: number}) {
  return (
    <div className="UserPage">
      <h2>User</h2>
      <h4>Change username</h4>
      <NameInput userId={props.userId} />
    </div>
  );
}

function NameInput(props: {userId: number}) {
  const userId = props.userId;
  const [name, setName] = useState("");

  return (
    <>
      <input
        placeholder="Type a username"
        value={name}
        onChange={event => {
          setName(event.target.value);
        }}
        onKeyPress={event => {
          if (event.key === "Enter") {
            sendUpdateName(userId, name);
          }
        }}
      />
      <button onClick={() => sendUpdateName(userId, name)}>
        Send
      </button>
    </>
  );
}
