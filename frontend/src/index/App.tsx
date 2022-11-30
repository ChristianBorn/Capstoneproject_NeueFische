import React, {useEffect, useState} from 'react';
import './css/App.css';
import {BrowserRouter, NavLink, Route, Routes} from "react-router-dom";
import StockOverview from "../stock/StockOverview";
import "../index/css/GeneralOverviewPage.css";
import HorseOverview from "../horses/HorseOverview";
import axios from "axios";
import ClipLoader from "react-spinners/ClipLoader";
import LoginPage from '../security/LoginPage';
import RegisterPage from "../security/RegisterPage";
import "../buttons/css/LogoutButton.css";
import "../buttons/css/GeneralButtonStyles.css";


function App() {
    const [userName, setUserDetails] = useState<string>();


    const fetchUsername = () => {
        axios.get("/api/app-users/me")
            .then(response => response.data)
            .then(setUserDetails);
    }

    const logout = () => {
        axios.get("/api/app-users/logout").then(fetchUsername)
    }

    useEffect(fetchUsername, [])


    if (userName === undefined) {
        return <ClipLoader
            size={150}
            aria-label="Loading Spinner"
            data-testid="loader"
        />
    }
    if (userName === "anonymousUser") {
        return <>

            <BrowserRouter>
                <Routes>
                    <Route path={"/*"} element={<LoginPage onLogin={fetchUsername}/>}></Route>
                    <Route path={"/registrieren"} element={<RegisterPage/>}></Route>
                </Routes>
            </BrowserRouter>
        </>
    }

    return (
        <div className="App">
            <header className="App-header">
            </header>
            <main>
                <button onClick={logout} className={"logout-button"}>Ausloggen</button>
                <BrowserRouter>
                    <div className={"floating-navbar"}>
                        <nav>
                        <NavLink to={"/pferde/ueberblick"}>Pferde</NavLink>
                            <NavLink to={"/"}>Einstaller</NavLink>
                            <NavLink to={"/lager/ueberblick"}>Lager</NavLink>
                            <NavLink to={"/"}>Account</NavLink>
                        </nav>
                    </div>
                    <Routes>
                        <Route path={"/lager/ueberblick"} element={<StockOverview/>}></Route>
                        <Route path={"/pferde/ueberblick"} element={<HorseOverview/>}></Route>
                    </Routes>
                </BrowserRouter>
            </main>
        </div>
    );
}

export default App;
