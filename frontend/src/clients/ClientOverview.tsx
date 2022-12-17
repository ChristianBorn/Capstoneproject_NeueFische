import React, {useEffect, useState} from 'react';
import axios from "axios";
import {ClientModel} from "./ClientModel";
import {BounceLoader} from "react-spinners";
import AddIcon from "../icons/AddIcon";
import DeleteIcon from "../icons/DeleteIcon";
import EditIcon from "../icons/EditIcon";
import AddClientModal from "./AddClientModal";
import {HorseModel} from "../horses/HorseModel";
import DeleteClientModal from "./DeleteClientModal";
import EditClientModal from "./EditClientModal";

function HorseOverview() {

    const [clients, setClients] = useState<ClientModel[]>()
    const [horses, setHorses] = useState<HorseModel[]>([])
    const [successMessage, setSuccessMessage] = useState<string>()
    const [idToDelete, setIdToDelete] = useState<string>("")
    const [clientToEdit, setClientToEdit] = useState<ClientModel>(
        {id: "", name: "", ownsHorse: []})

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
        getAllClients()
        getAllHorses()
    }

    const getAllClients = () => {
        axios.get("/clients/")
            .then((response) => response.data)
            .catch((error) => console.error("Error while getting clients:" + error))
            .then(setClients)
    }
    const getAllHorses = () => {
        axios.get("/horses/")
            .then((response) => response.data)
            .catch((error) => console.error("Error while getting horses:" + error))
            .then(setHorses)
    }
    useEffect(() => {
        getAllClients()
        getAllHorses()
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
            <AddClientModal modalIsOpen={openModal === "add"}
                            closeModal={closeModal}
                            reloadClients={getAllClients}
                            setSuccessMessage={setSuccessMessage}
                            horseList={horses}
                            registeredClients={clients}/>
            <DeleteClientModal modalIsOpen={openModal === "delete"}
                               closeModal={closeModal}
                               reloadClients={getAllClients}
                               setSuccessMessage={setSuccessMessage}
                               idToDelete={idToDelete}
                               reloadHorses={getAllHorses}/>
            <EditClientModal modalIsOpen={openModal === "edit"}
                             closeModal={closeModal}
                             reloadClients={getAllClients}
                             setSuccessMessage={setSuccessMessage}
                             clientToEdit={clientToEdit}
                             horseList={horses}
                             registeredClients={clients}/>

            {clients.length > 0 ?
                <>
                    <div className={"overview-table-wrapper"}>
                        <table>
                            <thead>
                            <tr>
                                <th>Name</th>
                                <th>Eingestallte/s Pferd/e</th>
                                <th>Aktionen</th>
                            </tr>
                            </thead>
                            <tbody>
                            {clients.map(client => {
                                return <tr key={client.id}>
                                    <td><strong>{client.name}</strong></td>
                                    <td>
                                        {client.ownsHorse && client.ownsHorse.map(horse => {
                                            return <p key={horse.id}>{horse.name}</p>
                                        })}
                                    </td>
                                    <td>
                                        <div className={"action-cell"}>
                                            <EditIcon onClickAction={openEditModal}
                                                      itemToEdit={client}/>
                                            <DeleteIcon onClickAction={openDeleteModal}
                                                        idToDelete={client.id}/>
                                        </div>
                                    </td>
                                </tr>
                            })}
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
