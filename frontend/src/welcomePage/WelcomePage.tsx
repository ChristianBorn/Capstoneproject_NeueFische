import React, {useCallback, useEffect, useState} from 'react';
import LinkedCard from "../structuralComponents/LinkedCard";
import "./css/LinkedCardCollection.css";
import axios from "axios";

function WelcomePage() {
    const [numberHorses, setNumberHorses] = useState<number>()
    const [numberClients, setNumberclients] = useState<number>()
    const [numberStockItems, setnumberStockItems] = useState<number>()

    const getAllHorses = useCallback(() => {
        axios.get("/horses/")
            .then((response) => response.data.length)
            .catch((error) => console.error("Error while getting Horses:" + error))
            .then(setNumberHorses)
    }, [])
    const getAllClients = useCallback(() => {
        axios.get("/clients/")
            .then((response) => response.data.length)
            .catch((error) => console.error("Error while getting clients:" + error))
            .then(setNumberclients)
    }, [])
    const getAllStockItems = useCallback(() => {
        axios.get("/stock/")
            .then((response) => response.data.length)
            .catch((error) => console.error("Error while getting Stockitems:" + error))
            .then(setnumberStockItems)
    }, [])


    useEffect(() => {
        getAllHorses()
        getAllClients()
        getAllStockItems()
    }, [getAllHorses, getAllClients, getAllStockItems])
    return (<>
            <h2>Willkommen</h2>
            {numberClients !== undefined && numberHorses !== undefined && numberStockItems !== undefined &&
                <div className={"linked-card-collection"}>
                    <LinkedCard title={"Pferde"} link={"/pferde/ueberblick"}
                                furtherInformation={"Eingestallte Pferde: " + numberHorses}/>
                    <LinkedCard title={"Einstaller"} link={"/einstaller/ueberblick"}
                                furtherInformation={"Registrierte Einstaller: " + numberClients}/>
                    <LinkedCard title={"Lager"} link={"/lager/ueberblick"}
                                furtherInformation={"Lagerpositionen: " + numberStockItems}/>
                </div>}
        </>
    );
}

export default WelcomePage;