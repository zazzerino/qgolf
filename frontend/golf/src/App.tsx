import React, {useReducer} from 'react';
import {BrowserRouter} from "react-router-dom";
import {Navbar} from "./Navbar";
import {INITIAL_STATE, rootReducer} from "./reducer";

function App() {
  const [state, dispatch] = useReducer(rootReducer, INITIAL_STATE);
  const {user} = state;

  return (
    <div className="App">
      <BrowserRouter>
        <Navbar user={user} />
      </BrowserRouter>
    </div>
  );
}

export default App;
