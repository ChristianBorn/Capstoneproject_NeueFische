import React, {useEffect, useState} from 'react';
import {BrowserRouter, NavLink, Route, Routes} from "react-router-dom";
import axios from "axios";
import {BounceLoader} from "react-spinners";
import StockOverview from "../stock/StockOverview";
import HorseOverview from "../horses/HorseOverview";
import LoginPage from '../security/LoginPage';
import RegisterPage from "../security/RegisterPage";
import "../buttons/css/LogoutButton.css";
import "../buttons/css/GeneralButtonStyles.css";
import "../index/css/GeneralOverviewPage.css";
import './css/App.css';
import ClientOverview from "../clients/ClientOverview";


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
        return <BounceLoader
            size={150}
            aria-label="Loading Spinner"
            data-testid="loader"
            color="#36d7b7"
            cssOverride={{
                margin: "0 auto"
            }}
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
            <main>
                <BrowserRouter>
                    <div className={"floating-navbar"}>
                        <nav>
                            <NavLink to={"/pferde/ueberblick"}>Pferde</NavLink>
                            <NavLink to={"/einstaller/ueberblick"}>Einstaller</NavLink>
                            <NavLink to={"/lager/ueberblick"}>Lager</NavLink>
                        </nav>
                    </div>
                    <div className={"logout-container"}>
                        <p>Eingeloggt als: {userName}</p>
                        <button onClick={logout} className={"logout-button"}>Ausloggen</button>
                    </div>
                    <Routes>
                        <Route path={"/lager/ueberblick"} element={<StockOverview/>}></Route>
                        <Route path={"/pferde/ueberblick"} element={<HorseOverview/>}></Route>
                        <Route path={"/einstaller/ueberblick"} element={<ClientOverview/>}></Route>
                    </Routes>
                </BrowserRouter>
            </main>
        </div>
    );
}

export default App;
