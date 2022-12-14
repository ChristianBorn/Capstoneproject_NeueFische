import React, {useEffect, useState} from 'react';
import axios from "axios";
import {ClientModel} from "./ClientModel";
import {BounceLoader} from "react-spinners";
import AddIcon from "../icons/AddIcon";
import DeleteIcon from "../icons/DeleteIcon";
import EditIcon from "../icons/EditIcon";

function HorseOverview() {

    const [clients, setClients] = useState<ClientModel[]>([])
    const [successMessage, setSuccessMessage] = useState<string>()
    const [idToDelete, setIdToDelete] = useState<string>("")
    const [clientToEdit, setClientToEdit] = useState<ClientModel>(
        {id: "", name: "", owns: [], clientSince: ""})

    const [openModal, setOpenModal] = useState<"add" | "edit" | "delete">()


    const openAddModal = () => {
        setOpenModal("add")
        setSuccessMessage("")
    }
    const openDeleteModal = (id: string) => {
        setOpenModal("delete")
        setIdToDelete(id)
        setSuccessMessage("")
    }
    const openEditModal = (horseToEdit: any) => {
        setOpenModal("edit")
        setClientToEdit(horseToEdit)
        setSuccessMessage("")
    }
    const closeModal = () => {
        setOpenModal(undefined)
    }

    const getAllClients = () => {
        axios.get("/clients/")
            .then((response) => response.data)
            .catch((error) => console.error("Error while getting clients:" + error))
            .then(setClients)
    }

    useEffect(() => {
        getAllClients()
    }, [])


    if (clients === undefined) {
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
            {/*<AddHorseModal modalIsOpen={openModal === "add"}*/}
            {/*               closeModal={closeModal}*/}
            {/*               reloadHorses={getAllclients}*/}
            {/*               setSuccessMessage={setSuccessMessage}/>*/}
            {/*<DeleteHorseModal modalIsOpen={openModal === "delete"}*/}
            {/*                  closeModal={closeModal}*/}
            {/*                  reloadHorses={getAllclients}*/}
            {/*                  setSuccessMessage={setSuccessMessage}*/}
            {/*                  idToDelete={idToDelete}/>*/}
            {/*<EditHorseModal modalIsOpen={openModal === "edit"}*/}
            {/*                closeModal={closeModal}*/}
            {/*                reloadHorses={getAllclients}*/}
            {/*                setSuccessMessage={setSuccessMessage}*/}
            {/*                horseToEdit={clientToEdit}/>*/}
            {/*<AddConsumptionModal modalIsOpen={openModal === "addConsumption"}*/}
            {/*                     closeModal={closeModal}*/}
            {/*                     reloadHorses={getAllclients}*/}
            {/*                     stockItemList={stockItems}*/}
            {/*                     setSuccessMessage={setSuccessMessage}*/}
            {/*                     selectedHorse={clientToEdit}/>*/}
            {clients.length > 0 ?
                <>
                    <div className={"overview-table-wrapper"}>
                        <table>

                            <thead>
                            <tr>
                                <th>Name</th>
                                <th>Eingestallte/s Pferd/e</th>
                                <th>Einstaller seit</th>
                                <th>Aktionen</th>
                            </tr>
                            </thead>
                            <tbody>
                            {clients.map(client => {
                                return <tr key={client.id}>
                                    <td><strong>{client.name}</strong></td>
                                    <td>{client.owns && client.owns.map(horse => {
                                        return <p>horse</p>
                                    })}</td>
                                    <td>{client.clientSince}</td>
                                    <td>
                                        <div className={"action-cell"}>
                                            <EditIcon onClickAction={openEditModal}
                                                      itemToEdit={client}/>
                                            <DeleteIcon onClickAction={openDeleteModal}
                                                        idToDelete={client.id}/>
                                        </div>
                                    </td>
                                </tr>
                            })
                            }
                            </tbody>
                        </table>

                    </div>
                    {successMessage && <div className={"success-message"}>{successMessage}</div>}
                </>
                :
                <div>
                    {successMessage && <div className={"success-message"}>{successMessage}</div>}
                    <p>Keine Einstaller angelegt</p>
                </div>

            }
            <AddIcon openModal={openAddModal} title={"Neuen Einstaller hinzufügen"}/>
        </>
    );
}

export default HorseOverview;
