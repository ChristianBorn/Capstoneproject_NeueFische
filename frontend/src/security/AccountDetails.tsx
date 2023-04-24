import React, {useEffect, useState} from 'react';
import axios from "axios";
import {AccountModel} from "./models/AccountModel";
import {BounceLoader} from "react-spinners";


function AccountDetails() {
    const [accountDetails, setAccountDetails] = useState<AccountModel>()

    useEffect(() => {
        const fetchAccountDetails =
            () => {
                axios.get("/api/app-users/account-details/")
                    .then((response) => response.data)
                    .catch((error) => console.error("Error while getting account details:" + error))
                    .then(setAccountDetails)
            }
        fetchAccountDetails()
    }, [])

    if (accountDetails === undefined) {
        return <BounceLoader
            size={100}
            aria-label="Loading Spinner"
            data-testid="loader"
            color="#36d7b7"
            cssOverride={{
                margin: "0 auto"
            }}
        />
    }

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