import React, {useState} from "react";
import axios from "axios";
import {Link} from "react-router-dom";
import "./css/LoginPage.css"
import FieldLabelGroup from "../structuralComponents/FieldLabelGroup";

type LoginPageProps = {
    onLogin: () => void,
}
export default function LoginPage(props: LoginPageProps) {
    const [username, setUsername] = useState<string>("")
    const [password, setPassword] = useState<string>("")
    const [loginFailed, setLoginFailed] = useState<string>()

    const login = () => {
        axios.get("/api/app-users/login", {
                auth: {
                    username,
                    password
                }
            }
        ).catch(() => {
            setLoginFailed("Login fehlgeschlagen, Username oder Passwort stimmen nicht")
        })
            .then(props.onLogin)
    }


    return (
        <>
            <div className={"login-form-area"}>
                <h2>Login</h2>
                <FieldLabelGroup>
                    <label htmlFor={"username"}>Username</label>
                    <input required id={"username"} type={"text"} onChange={event => setUsername(event.target.value)}/>
                </FieldLabelGroup>
                <FieldLabelGroup>
                    <label htmlFor={"password"}>Passwort</label>
                    <input required id={"password"} type={"password"}
                           onChange={event => setPassword(event.target.value)}/>
                </FieldLabelGroup>
                {loginFailed &&
                    <p className={"error-message"}>{loginFailed}</p>}
                <button className={"submit-button"} onClick={() => login()}>Login</button>
                <Link to="/registrieren">Registrieren</Link>
            </div>
        </>

    )
}