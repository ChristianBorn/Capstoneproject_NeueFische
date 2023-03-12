import React, {useEffect, useState} from 'react';
import LinkedCard from "../structuralComponents/LinkedCard";
import "./css/LinkedCardCollection.css";
import axios from "axios";

function WelcomePage() {
    const [numberHorses, setNumberHorses] = useState<number>(0)
    const getAllHorses = () => {
        axios.get("/horses/")
            .then((response) => response.data)
            .then((data) => data.length)
            .catch((error) => console.error("Error while getting Horses:" + error))
            .then(setNumberHorses)
    }
    useEffect(
        getAllHorses, []
    )
    return (<>
            <div>Willkommen</div>
            <div className={"linked-card-collection"}>
                <LinkedCard title={"Pferde"} link={"/pferde/ueberblick"}
                            furtherInformation={"Eingestallte Pferde: " + numberHorses}/>
                <LinkedCard title={"Einstaller"} link={"/einstaller/ueberblick"}
                            furtherInformation={"Registrierte Einstaller: " + 0}/>
            </div>
        </>
    );
}

export default WelcomePage;