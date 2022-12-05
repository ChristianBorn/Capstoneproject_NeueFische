import React, {useEffect, useState} from 'react';
import axios from "axios";
import {HorseModel} from "./HorseModel";
import {BounceLoader} from "react-spinners";
import AddIcon from "../icons/AddIcon";
import AddHorseModal from "./AddHorseModal";
import DeleteIcon from "../icons/DeleteIcon";
import DeleteHorseModal from "./DeleteItemModal";
import EditHorseModal from "./EditItemModal";
import EditIcon from "../icons/EditIcon";
import {StockItemModel} from "../stock/StockItemModel";
import AddConsumptionModal from "./AddConsumptionModal";

function HorseOverview() {

    const [horses, setHorses] = useState<HorseModel[]>([])
    const [stockItems, setStockItems] = useState<StockItemModel[]>([])
    const [addModalIsOpen, setAddModalIsOpen] = useState<boolean>(false)
    const [successMessage, setSuccessMessage] = useState<string>()
    const [deleteModalIsOpen, setDeleteModalIsOpen] = useState<boolean>(false)
    const [editModalIsOpen, setEditModalIsOpen] = useState<boolean>(false)
    const [addConsumptionModalIsOpen, setAddConsumptionModalIsOpen] = useState<boolean>(false)
    const [idToDelete, setIdToDelete] = useState<string>("")
    const [horseToEdit, setHorseToEdit] = useState<HorseModel>({
        id: "", name: "", owner: "", consumption: []
    })

    const openAddModal = () => {
        setAddModalIsOpen(true)
        setSuccessMessage("")
    }
    const openDeleteModal = (id: string) => {
        setDeleteModalIsOpen(true)
        setIdToDelete(id)
        setSuccessMessage("")
    }
    const openEditModal = (horseToEdit: any) => {
        setEditModalIsOpen(true)
        setHorseToEdit(horseToEdit)
        setSuccessMessage("")
    }
    const openAddConsumptionModal = (horseToEdit: any) => {
        setAddConsumptionModalIsOpen(true)
        setHorseToEdit(horseToEdit)
        setSuccessMessage("")
    }
    const closeModal = () => {
        setAddModalIsOpen(false)
        setDeleteModalIsOpen(false)
        setEditModalIsOpen(false)
        setAddConsumptionModalIsOpen(false)
    }
    const getAllStockItems = () => {
        axios.get("/stock/")
            .then((response) => response.data)
            .catch((error) => console.error("Error while getting Stockitems:" + error))
            .then(setStockItems)
            .then(() => console.log(stockItems))
    }

    const getAllHorses = () => {
        axios.get("/horses/")
            .then((response) => response.data)
            .catch((error) => console.error("Error while getting Horses:" + error))
            .then(setHorses)

    }

    useEffect(() => {
        getAllHorses()
        getAllStockItems()
    }, [])


    if (horses === undefined) {
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
        <>
            <AddHorseModal modalIsOpen={addModalIsOpen}
                           closeModal={closeModal}
                           reloadHorses={getAllHorses}
                           setSuccessMessage={setSuccessMessage}
            />
            <DeleteHorseModal modalIsOpen={deleteModalIsOpen}
                              closeModal={closeModal}
                              reloadHorses={getAllHorses}
                              setSuccessMessage={setSuccessMessage}
                              idToDelete={idToDelete}/>
            <EditHorseModal modalIsOpen={editModalIsOpen}
                            closeModal={closeModal}
                            reloadHorses={getAllHorses}
                            setSuccessMessage={setSuccessMessage}
                            horseToEdit={horseToEdit}/>
            <AddConsumptionModal
                stockItemList={stockItems}
                selectedHorse={horseToEdit}
                modalIsOpen={addConsumptionModalIsOpen}
                closeModal={closeModal}
                reloadHorses={getAllHorses}
                setSuccessMessage={setSuccessMessage}/>
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
                                    <td><strong>{horse.name}</strong></td>
                                    <td>{horse.owner}</td>
                                    <td>
                                        <button onClick={() => openAddConsumptionModal(horse)}></button>
                                        {horse.consumption.length > 0 ? horse.consumption
                                                .map(consumptionObject => {
                                                    return <div
                                                        key={consumptionObject.id}>{consumptionObject.name}: {consumptionObject.dailyConsumption}
                                                        <abbr title={"Kilogramm"}>kg</abbr><br/></div>
                                                })
                                            : <p>Keine Verbräuche angelegt</p>}
                                    </td>
                                    {/*<td>{Array.from(horse.consumption.values())
                                        .map(consumptionObject => {
                                            return <div
                                                key={consumptionObject.id}>{consumptionObject.name}: {consumptionObject.dailyConsumption}
                                                <abbr title={"Kilogramm"}>kg</abbr><br/></div>
                                        })}</td>*/}
                                    <td>
                                        <div className={"action-cell"}>
                                            <EditIcon onClickAction={openEditModal}
                                                      itemToEdit={horse}/>
                                            <DeleteIcon onClickAction={openDeleteModal}
                                                        idToDelete={horse.id}/>
                                        </div>
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
