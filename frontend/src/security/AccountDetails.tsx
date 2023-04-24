import React, {useCallback, useEffect, useState} from 'react';
import axios from "axios";

type accountDetailsProps = {}

function AccountDetails(props: accountDetailsProps) {
    const [accountDetails, setAccountDetails] = useState<{
        id: String,
        username: String,
        role: String,
        eMail: String
    }>()

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

    const printUserDetails = () => {
        console.log(accountDetails?.id)
    }

    return (
        <div>
            <button title={"asdasdasdas"} onClick={printUserDetails}/>
        </div>
    );
}

export default AccountDetails;