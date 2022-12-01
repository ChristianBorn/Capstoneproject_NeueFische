import React, {useEffect, useState} from 'react';
import axios from "axios";
import {HorseModel} from "./HorseModel";
import ClipLoader from "react-spinners/ClipLoader";
import AddIcon from "../icons/AddIcon";
import AddHorseModal from "./AddHorseModal";

function HorseOverview() {

    const [horses, setHorses] = useState<HorseModel[]>()
    const [addModalIsOpen, setAddModalIsOpen] = useState<boolean>(false)
    const [successMessage, setSuccessMessage] = useState<string>()

    const openAddModal = () => {
        setAddModalIsOpen(true)
        setSuccessMessage("")
    }
    const closeModal = () => {
        setAddModalIsOpen(false)
    }


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
            <AddHorseModal modalIsOpen={addModalIsOpen}
                           closeModal={closeModal}
                           reloadHorses={getAllHorses}
                           setSuccessMessage={setSuccessMessage}
            />
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
                            {horses.map(horse => {
                                return <tr key={horse.id}>
                                    <td>{horse.name}</td>
                                    <td>{horse.owner}</td>
                                    <td>{horse.consumption
                                        .map(consumptionObject => {
                                            return <div
                                                key={consumptionObject.id}>{consumptionObject.name}: {consumptionObject.dailyConsumption}
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
                    {successMessage && <div className={"success-message"}>{successMessage}</div>}
                    <AddIcon openModal={openAddModal}/></>

                :
                <div>
                    {successMessage && <div className={"success-message"}>{successMessage}</div>}
                    <p>Keine Pferde im Stall</p>
                    <AddIcon openModal={openAddModal}/>
                </div>

            }
        </>
    );
}

export default HorseOverview;
