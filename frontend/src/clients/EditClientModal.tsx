import axios from 'axios';
import React, {useEffect, useState} from 'react';
import Modal from 'react-modal';
import {HorseModel} from "../horses/HorseModel";
import FieldLabelGroup from "../structuralComponents/FieldLabelGroup";
import CloseIcon from "../icons/CloseIcon";
import "../index/css/AddItemModal.css";
import "../buttons/css/SubmitButton.css";
import {ClientModel} from "./ClientModel";
import Select from "react-select";


type ModalProps = {
    modalIsOpen: boolean,
    closeModal: () => void,
    reloadClients: () => void,
    horseList: HorseModel[],
    registeredClients: ClientModel[],
    setSuccessMessage: (input: string) => void,
    clientToEdit: ClientModel
}

function EditClientModal(props: ModalProps) {
    const [editedClient, setEditedClient] = useState<ClientModel>(props.clientToEdit)
    const [horseSelectList, setHorseSelectList] = useState<{ label: string, value: HorseModel }[]>()
    const [clients, setClients] = useState<ClientModel[]>([])
    const [initialSelectedHorses, setInitialSelectedHorses] = useState<{ label: string, value: HorseModel }[]>([])


    useEffect(() => {
        setEditedClient(props.clientToEdit)
    }, [props.modalIsOpen, props.clientToEdit])

    useEffect(() => {
        const newHorseSelectList: { label: string, value: HorseModel }[] = []
        let horsesWithOwners: {}[] = []

        setClients(props.registeredClients)
        clients.map(client => client.ownsHorse
            .map(horse => horsesWithOwners.push(horse.id)))

        props.horseList.forEach(horse => {
            if (!horsesWithOwners.includes(horse.id)) {
                newHorseSelectList.push(
                    {"label": horse.name, "value": horse}
                )
            }
        })
        setHorseSelectList(newHorseSelectList)
        // eslint-disable-next-line
    }, [props.horseList, props.registeredClients])
    useEffect(() => {
        setInitialSelectedHorses(editedClient.ownsHorse.map(horse => ({label: horse.name, value: horse})))
    }, [editedClient])

    const saveNewClient = () => {
        axios.put("/clients/", editedClient)
            .catch((e) => console.error("PUT Error: " + e))
            .then(props.reloadClients)
            .then(props.closeModal)
            .then(() => setEditedClient(props.clientToEdit))
            .then(() => props.setSuccessMessage("Eintrag erfolgreich geändert"))

    }

    const handleSubmit = (event: React.SyntheticEvent<HTMLFormElement>) => {
        event.preventDefault()
        saveNewClient()
    }
    const handleChange = (event: any) => {
        const name = event.target.name;
        const value = event.target.value;
        setEditedClient({
            ...editedClient,
            [name]: value
        })
    }
    const handleSelectChange = (event: any) => {
        setInitialSelectedHorses(event)
        setEditedClient({
            ...editedClient,
            ownsHorse: event.map((item: { value: any; }) => item.value)
        })
    }


    return (
        <Modal
            isOpen={props.modalIsOpen}
            contentLabel="Edit Modal"
            ariaHideApp={false}
            onRequestClose={props.closeModal}
        >
            <CloseIcon closeModal={props.closeModal}/>
            <section>
                <h2>Einstaller bearbeiten</h2>
                <form onSubmit={handleSubmit}>

                    <FieldLabelGroup>
                        <label htmlFor={"name"}>Name</label>
                        <input value={editedClient.name} onChange={handleChange} required type={"text"} id={"name"}
                               name={"name"}/>
                    </FieldLabelGroup>

                    <FieldLabelGroup>
                        <label htmlFor={"ownsHorse"}>Besitzt</label>
                        <Select value={initialSelectedHorses} isMulti options={horseSelectList}
                                onChange={handleSelectChange} required id={"ownsHorse"}
                                name={"ownsHorse"}/>
                    </FieldLabelGroup>

                    <div className={"button-group"}>
                        <button className={"submit-button"} type={"submit"}>Änderungen speichern</button>
                    </div>
                </form>
            </section>
        </Modal>
    );
}

export default EditClientModal;
