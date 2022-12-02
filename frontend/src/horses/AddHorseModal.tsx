import axios from 'axios';
import React, {ChangeEvent, useState} from 'react';
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
}

function AddItemModal(props: ModalProps) {
    const [newHorse, setNewHorse] = useState<HorseModel>({
        id: "", name: "", owner: "", consumptionList: []
    })

    const handleSubmit = (event: React.SyntheticEvent<HTMLFormElement>) => {
        event.preventDefault()
        axios.post("/horses/", newHorse)
            .catch((e) => console.error("POST Error: " + e))
            .then(props.reloadHorses)
            .then(props.closeModal)
            .then(() => setNewHorse({id: "", name: "", owner: "", consumptionList: []}))
            .then(() => props.setSuccessMessage("Eintrag erfolgreich hinzugefügt"))
    }
    const handleChange = (event: ChangeEvent<HTMLInputElement>) => {
        const {name, value} = event.target
        setNewHorse({
            ...newHorse,
            [name]: value
        })
    }


    return (
        <Modal
            isOpen={props.modalIsOpen}
            contentLabel="Add Modal"
            ariaHideApp={false}
            onRequestClose={props.closeModal}
        >
            <CloseIcon closeModal={props.closeModal}/>
            <section>
                <h2>Neues Pferd anlegen</h2>
                <form onSubmit={handleSubmit}>

                    <FieldLabelGroup>
                        <label htmlFor={"name"}>Name</label>
                        <input onChange={handleChange} required type={"text"} id={"name"} name={"name"}/>
                    </FieldLabelGroup>

                    <FieldLabelGroup>
                        <label htmlFor={"owner"}>Besitzer</label>
                        <input onChange={handleChange} required
                               type={"text"}
                               id={"owner"} name={"owner"}/>
                    </FieldLabelGroup>

                    <div className={"button-group"}>
                        <button className={"submit-button"} type={"submit"}>Speichern</button>
                        <button className={"abort-button"} onClick={props.closeModal}>Abbrechen</button>
                    </div>
                </form>
            </section>
        </Modal>
    );
}

export default AddItemModal;
