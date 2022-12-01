import axios from 'axios';
import React, {useEffect, useState} from 'react';
import Modal from 'react-modal';
import {HorseModel} from "./HorseModel";
import FieldLabelGroup from "../structuralComponents/FieldLabelGroup";
import CloseIcon from "../icons/CloseIcon";
import "../index/css/AddItemModal.css";
import "../buttons/css/SubmitButton.css";


type ModalProps = {
    modalIsOpen: boolean,
    closeModal: () => void,
    reloadHorses: () => void,
    setSuccessMessage: (input: string) => void,
    horseToEdit: HorseModel
}

function EditHorseModal(props: ModalProps) {
    const [editedHorse, setEditedHorse] = useState<HorseModel>(props.horseToEdit)
    useEffect(() => setEditedHorse(props.horseToEdit), [props.horseToEdit])


    const saveNewHorse = () => {
        axios.put("/horses/", editedHorse)
            .catch((e) => console.error("PUT Error: " + e))
            .then(props.reloadHorses)
            .then(props.closeModal)
            .then(() => setEditedHorse(props.horseToEdit))
            .then(() => props.setSuccessMessage("Eintrag erfolgreich geändert"))

    }

    const handleSubmit = (event: React.SyntheticEvent<HTMLFormElement>) => {
        event.preventDefault()
        saveNewHorse()
    }
    const handleChange = (event: any) => {
        const name = event.target.name;
        const value = event.target.value;
        setEditedHorse({
            ...editedHorse,
            [name]: value
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
                <h2>Pferd bearbeiten</h2>
                <form onSubmit={handleSubmit}>

                    <FieldLabelGroup>
                        <label htmlFor={"name"}>Name</label>
                        <input value={editedHorse.name} onChange={handleChange} required type={"text"} id={"name"}
                               name={"name"}/>
                    </FieldLabelGroup>

                    <FieldLabelGroup>
                        <label htmlFor={"owner"}>Besitzer</label>
                        <input value={editedHorse.owner} onChange={handleChange} required
                               type={"text"}
                               id={"owner"} name={"owner"}/>
                    </FieldLabelGroup>

                    <div className={"button-group"}>
                        <button className={"submit-button"} type={"submit"}>Änderungen speichern</button>
                    </div>
                </form>
            </section>
        </Modal>
    );
}

export default EditHorseModal;
