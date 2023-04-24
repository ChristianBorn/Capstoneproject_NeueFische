import axios from 'axios';
import React, {useEffect, useState} from 'react';
import Modal from 'react-modal';
import {StockItemModel} from "./models/StockItemModel";
import Form3Rows from "../structuralComponents/Form3Rows";
import FieldLabelGroup from "../structuralComponents/FieldLabelGroup";
import CloseIcon from "../icons/CloseIcon";
import "../index/css/AddItemModal.css";
import "../buttons/css/SubmitButton.css";

type ModalProps = {
    modalIsOpen: boolean,
    closeModal: () => void,
    reloadStockItems: () => void,
    setSuccessMessage: (input: string) => void,
}

function AddItemModal(props: ModalProps) {
    const [errorMessages, setErrorMessages] = useState({
        name: "", pricePerKilo: "", amountInStock: ""
    })
    const [newStockItem, setNewStockItem] = useState<StockItemModel>({
        id: "", name: "", amountInStock: 0, pricePerKilo: 0, type: ""
    })
    useEffect(() => {
        setErrorMessages({name: "", pricePerKilo: "", amountInStock: ""})
    }, [props.closeModal])

    const handleSubmit = (event: React.SyntheticEvent<HTMLFormElement>) => {
        event.preventDefault()
        axios.post("/stock/", newStockItem)
            .then(response => response.data)
            .catch((e) => {
                if (e.response.status === 409) {
                    setErrorMessages({
                        ...errorMessages,
                        name: e.response.data.message
                    })
                }
                if (e.response.status === 400) {
                    setErrorMessages({
                        ...errorMessages,
                        [e.response.data.fieldName]: e.response.data.errorMessage
                    })
                }
                console.error("POST Error: " + e)
                throw e
            })
            .then(props.reloadStockItems)
            .then(props.closeModal)
            .then(() => setNewStockItem({id: "", name: "", amountInStock: 0, pricePerKilo: 0, type: ""}))
            .then(() => props.setSuccessMessage("Eintrag erfolgreich hinzugefügt"))
    }

    const handleChange = (event: any) => {
        const name = event.target.name;
        const value = event.target.value;

        setNewStockItem({
            ...newStockItem,
            [name]: value
        })
    }

    return (
        <Modal
            isOpen={props.modalIsOpen}
            contentLabel="Add Modal"
            ariaHideApp={false}
            onRequestClose={props.closeModal}>
            <CloseIcon closeModal={props.closeModal}/>
            <section>
                <h2>Neue Position dem Lager hinzufügen</h2>
                <form onSubmit={handleSubmit}>

                    <FieldLabelGroup>
                        <label htmlFor={"name"}>Name/Bezeichnung</label>
                        <input onChange={handleChange} required type={"text"} id={"name"} name={"name"}/>
                        {errorMessages.name &&
                            <div className={"message-container"}><p
                                className={"error-message"}>{errorMessages.name}</p></div>}
                    </FieldLabelGroup>

                    <Form3Rows>
                        <FieldLabelGroup>
                            <label htmlFor={"price"}>Preis pro <abbr title={"Kilogramm"}>kg</abbr></label>
                            <input onChange={handleChange} placeholder={"0"}
                                   step={"0.1"} min={"0"} required
                                   type={"number"}
                                   id={"price"} name={"pricePerKilo"}/>
                            {errorMessages.pricePerKilo &&
                                <div className={"message-container"}><p
                                    className={"error-message"}>{errorMessages.pricePerKilo}</p></div>}
                        </FieldLabelGroup>

                        <FieldLabelGroup>
                            <label htmlFor={"amount"}>Menge in <abbr title={"Kilogramm"}>kg</abbr></label>
                            <input onChange={handleChange} placeholder={"0"}
                                   step={"0.1"} min={"0"} required
                                   type={"number"}
                                   id={"amount"} name={"amountInStock"}/>
                            {errorMessages.amountInStock &&
                                <div className={"message-container"}><p
                                    className={"error-message"}>{errorMessages.amountInStock}</p></div>}
                        </FieldLabelGroup>

                        <FieldLabelGroup>
                            <label htmlFor={"type"}>Typ</label>
                            <select onChange={handleChange} required id={"type"} name={"type"}>
                                <option value="" selected disabled hidden>Bitte auswählen</option>
                                <option value={"Futter"}>Futter</option>
                                <option value={"Einstreu"}>Einstreu</option>
                            </select>
                        </FieldLabelGroup>
                    </Form3Rows>

                    <div className={"button-group"}>
                        <button className={"submit-button"} type={"submit"}>Einlagern</button>
                        <button className={"abort-button"} onClick={props.closeModal}>Abbrechen</button>
                    </div>
                </form>
            </section>
        </Modal>
    );
}

export default AddItemModal;
