import axios from 'axios';
import React, {useState} from 'react';
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
        id: "", name: "", owner: "", consumption: []
    })


    const saveNewHorse = () => {
        axios.post("/horses/", newHorse)
            .catch((e) => console.error("POST Error: " + e))
            .then(props.reloadHorses)
            .then(props.closeModal)
            .then(() => setNewHorse({id: "", name: "", owner: "", consumption: []}))
            .then(() => props.setSuccessMessage("Eintrag erfolgreich hinzugefügt"))

    }

    const handleSubmit = (event: React.SyntheticEvent<HTMLFormElement>) => {
        event.preventDefault()
        saveNewHorse()
    }
    const handleChange = (event: any) => {
        const name = event.target.name;
        const value = event.target.value;
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
                    </div>
                </form>
            </section>
        </Modal>
    );
}

export default AddItemModal;
