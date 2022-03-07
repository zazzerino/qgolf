import {User} from "../types";
import React, {Dispatch, SetStateAction, useState} from "react";
import {sendUpdateNameMessage} from "../websocket";

interface NameInputProps {
  userId: number;
  name: string;
  setName: Dispatch<SetStateAction<string>>;
}

function NameInput(props: NameInputProps) {
  const {name, setName, userId} = props;

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
            sendUpdateNameMessage(userId, name);
          }
        }}
      />
      <button onClick={() => sendUpdateNameMessage(userId, name)}>
        Send
      </button>
    </>
  );
}

export function UserPage(props: {user: User}) {
  const [name, setName] = useState("");
  const {user} = props;

  return (
    <div className="UserPage">
      <h2>User</h2>
      <h4>Change username</h4>
      <NameInput
        name={name}
        setName={setName}
        userId={user.id}
      />
    </div>
  );
}
