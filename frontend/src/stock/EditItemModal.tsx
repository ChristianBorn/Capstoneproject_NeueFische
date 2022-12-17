import axios from 'axios';
import React, {useEffect, useState} from 'react';
import Modal from 'react-modal';
import {StockItemModel} from "./StockItemModel";
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
    itemToEdit: StockItemModel
}

function EditItemModal(props: ModalProps) {

    const [errorMessages, setErrorMessages] = useState({
        name: "", pricePerKilo: "", amountInStock: ""
    })
    const [editedStockItem, setEditedStockItem] = useState<StockItemModel>(props.itemToEdit)
    useEffect(() => setEditedStockItem(props.itemToEdit), [props.itemToEdit])
    useEffect(() => {
        setErrorMessages({name: "", pricePerKilo: "", amountInStock: ""})
    }, [props.closeModal])

    const saveNewStockitem = () => {
        axios.put("/stock/", editedStockItem)
            .catch((e) => {
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
            .then(() => setEditedStockItem(props.itemToEdit))
            .then(() => props.setSuccessMessage("Eintrag erfolgreich geändert"))
    }

    const handleSubmit = (event: React.SyntheticEvent<HTMLFormElement>) => {
        event.preventDefault()
        saveNewStockitem()
    }
    const handleChange = (event: any) => {
        const name = event.target.name;
        const value = event.target.value;
        setEditedStockItem({
            ...editedStockItem,
            [name]: value
        })
    }

    return (
        <Modal
            isOpen={props.modalIsOpen}
            contentLabel="Edit Modal"
            ariaHideApp={false}
            onRequestClose={props.closeModal}>
            <CloseIcon closeModal={props.closeModal}/>
            <section>
                <h2>Position bearbeiten</h2>
                <form onSubmit={handleSubmit}>

                    <div className={"field-label-group"}>
                        <label htmlFor={"name"}>Name/Bezeichnung</label>
                        <input value={editedStockItem.name} onChange={handleChange} required type={"text"} id={"name"}
                               name={"name"}/>
                    </div>

                    <Form3Rows>
                        <FieldLabelGroup>
                            <label htmlFor={"price"}>Preis pro <abbr title={"Kilogramm"}>kg</abbr></label>
                            <input value={editedStockItem.pricePerKilo} onChange={handleChange} placeholder={"0"}
                                   step={"0.1"} min={"0"} required
                                   type={"number"}
                                   id={"price"} name={"pricePerKilo"}/>
                            {errorMessages.pricePerKilo &&
                                <div className={"message-container"}><p
                                    className={"error-message"}>{errorMessages.pricePerKilo}</p></div>}
                        </FieldLabelGroup>

                        <FieldLabelGroup>
                            <label htmlFor={"amount"}>Menge in <abbr title={"Kilogramm"}>kg</abbr></label>
                            <input value={editedStockItem.amountInStock} onChange={handleChange} placeholder={"0"}
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
                                <option value={editedStockItem.type} selected disabled hidden>Bisheriger
                                    Typ: {editedStockItem.type}</option>
                                <option value={"Futter"}>Futter</option>
                                <option value={"Einstreu"}>Einstreu</option>
                            </select>
                        </FieldLabelGroup>
                    </Form3Rows>

                    <div className={"button-group"}>
                        <button className={"submit-button"} type={"submit"}>Änderungen speichern</button>
                    </div>
                </form>
            </section>
        </Modal>
    );
}

export default EditItemModal;
