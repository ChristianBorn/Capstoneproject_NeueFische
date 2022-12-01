import React, {useEffect, useState} from 'react';
import axios from "axios";
import {HorseModel} from "./HorseModel";
import ClipLoader from "react-spinners/ClipLoader";

function HorseOverview() {

    const [horses, setHorses] = useState<HorseModel[]>()


    const getAllHorses = () => {
        axios.get("/horses/")
            .then((response) => response.data)
            .catch((error) => console.error("Error while getting Horses:" + error))
            .then(setHorses)
    }


    useEffect(() => {
        getAllHorses()
    }, [])


    if (horses === undefined) {
        return <ClipLoader
            size={150}
            aria-label="Loading Spinner"
            data-testid="loader"
        />
    }

    return (
        <>

            {horses.length > 0 ?
                <>
                    <div className={"overview-table-wrapper"}>
                        <table>

                            <thead>
                            <tr>
                                <th>Name</th>
                                <th>Besitzer</th>
                                <th>Täglicher Verbrauch</th>
                                <th>Aktionen</th>
                            </tr>
                            </thead>
                            <tbody>
                            {horses.map(singleItem => {
                                return <tr key={singleItem.id}>
                                    <td>{singleItem.name}</td>
                                    <td>{singleItem.owner}</td>
                                    <td>{singleItem.consumption
                                        .map(singleConsumptionObject => {
                                            return <div
                                                key={singleConsumptionObject.id}>{singleConsumptionObject.name}: {singleConsumptionObject.dailyConsumption}
                                                <abbr title={"Kilogramm"}>kg</abbr><br/></div>
                                        })}</td>
                                    <td>
                                        <div className={"action-cell"}></div>
                                    </td>
                                </tr>
                            })
                            }
                            </tbody>
                        </table>

                    </div>
                </>
                :
                <div>
                    <p>Keine Pferde im Stall</p>
                </div>

            }
        </>
    );
}

export default HorseOverview;
