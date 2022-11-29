import React, {useState} from "react";
import axios from "axios";
import {Link} from "react-router-dom";

type LoginPageProps = {
    onLogin: () => void,
}
export default function LoginPage(props: LoginPageProps) {
    const [username, setUsername] = useState<string>("")
    const [password, setPassword] = useState<string>("")

    const login = () => {
        axios.get("/api/app-users/login", {
                auth: {
                    username,
                    password
                }
            }
        ).then(props.onLogin)
    }


    return (
        <>
            <label htmlFor={"username"}>Username</label>
            <input required id={"username"} type={"text"} onChange={event => setUsername(event.target.value)}/>
            <label htmlFor={"password"}>Password</label>
            <input required id={"password"} type={"password"} onChange={event => setPassword(event.target.value)}/>
            <button onClick={() => login()}>Login</button>
            <Link to="/registrieren">Registrieren</Link>

        </>

    )
}