import React from 'react';
import './css/App.css';
import {BrowserRouter, NavLink, Route, Routes} from "react-router-dom";
import StockOverview from "../stock/StockOverview";

function App() {
  return (
    <div className="App">
      <header className="App-header">

      </header>
      <main>
        <BrowserRouter>
            <nav><NavLink to={"/lager/ueberblick"}>Lager</NavLink></nav>
            <Routes>
                <Route path={"/lager/ueberblick"} element={<StockOverview/>}></Route>
            </Routes>
        </BrowserRouter>
      </main>
    </div>
  );
}

export default App;
