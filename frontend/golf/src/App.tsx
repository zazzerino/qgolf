import React, {useEffect, useReducer} from 'react';
import {BrowserRouter, Routes, Route} from "react-router-dom";
import {Navbar} from "./Navbar";
import {INITIAL_STATE, rootReducer} from "./reducer";
import {HomePage} from "./page/HomePage";
import {UserPage} from "./page/UserPage";
import {GamePage} from "./page/GamePage";
import {initWebsocket} from "./websocket";

function App() {
  const [state, dispatch] = useReducer(rootReducer, INITIAL_STATE);
  const {user, game} = state;

  useEffect(() => {
    initWebsocket(dispatch);
  }, []);

  return (
    <div className="App">
      <BrowserRouter>
        <Navbar user={user} />
        <Routes>
          <Route path="/" element={<HomePage />} />
          <Route path="/user" element={<UserPage userId={user.id} />} />
          <Route path="/game" element={<GamePage user={user} game={game} />} />
        </Routes>
      </BrowserRouter>
    </div>
  );
}

export default App;
