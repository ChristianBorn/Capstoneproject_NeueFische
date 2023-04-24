import React, {useCallback, useEffect, useState} from 'react';
import axios from "axios";
import {AccountModel} from "./models/AccountModel";


function AccountDetails() {
    const [accountDetails, setAccountDetails] = useState<AccountModel>()

    const fetchAccountDetails = useCallback(
        () => {
            axios.get("/api/app-users/account-details/")
                .then((response) => response.data)
                .catch((error) => console.error("Error while getting account details:" + error))
                .then(setAccountDetails)
        }, [])

    useEffect(() => {
        fetchAccountDetails()
    })

    return (
        <div>
            {accountDetails && <>
                <div>User ID: {accountDetails.id}</div>
                <div>Username: {accountDetails.username}</div>
                <div>Rolle: {accountDetails.role}</div>
                <div>E-Mail Adresse: {accountDetails.eMail ? accountDetails.eMail : "Keine Adresse angegeben"}</div>
            </>}
        </div>
    );
}

export default AccountDetails;