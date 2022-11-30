import React, {useState} from 'react';
import axios from "axios";
import {Link} from "react-router-dom";
import FieldLabelGroup from "../structuralComponents/FieldLabelGroup";
import "./css/RegisterPage.css"


function RegisterPage() {
    const [username, setUsername] = useState<string>("")
    const [rawPassword, setRawPassword] = useState<string>("")
    const [registrationErrorMessage, setRegistrationErrorMessage] = useState({
        rawPassword: undefined,
        username: undefined,
        userAlreadyExists: undefined
    })
    const [successMessage, setSuccessMessage] = useState<string>("")

    const register = () => {
        axios.post("/api/app-users", {username, rawPassword})
            .then(response => response.data)
            .then(data => setSuccessMessage(data))
            .then(() => setRegistrationErrorMessage({
                rawPassword: undefined,
                username: undefined,
                userAlreadyExists: undefined,
            }))
            .catch(response => {
                setRegistrationErrorMessage(response.response.data)
                setSuccessMessage("")
            })

    }


    return <>
        <div className={"login-form-area"}>
            <h2>Registrierung</h2>
            <FieldLabelGroup>
                <label htmlFor={"username"}>Username</label>
                <input required id={"username"} type={"text"} onChange={event => setUsername(event.target.value)}/>
                {registrationErrorMessage.username &&
                    <div className={"message-container"}><p
                        className={"error-message"}>{registrationErrorMessage.username}</p></div>}

            </FieldLabelGroup>
            <FieldLabelGroup>
                <label htmlFor={"password"}>Passwort</label>
                <input required id={"password"} type={"password"}
                       onChange={event => setRawPassword(event.target.value)}/>
                {registrationErrorMessage.rawPassword &&
                    <div className={"message-container"}><p
                        className={"error-message"}>{registrationErrorMessage.rawPassword}</p></div>}

            </FieldLabelGroup>
            {registrationErrorMessage.userAlreadyExists &&
                <div className={"message-container"}><p
                    className={"error-message"}>{registrationErrorMessage.userAlreadyExists}</p></div>}
            <button className={"submit-button"} onClick={() => register()}>Registrieren</button>
            {successMessage &&
                <div className={"message-container"}><p className={"success-message"}>{successMessage}</p></div>}


            <Link to="/">Zurück zu login</Link>
        </div>
    </>
}

export default RegisterPage;