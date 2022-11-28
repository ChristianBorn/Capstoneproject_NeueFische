import axios from 'axios';
import React, {useEffect, useState} from 'react';
import Modal from 'react-modal';
import {StockItemModel} from "./StockItemModel";
import "./css/AddItemModal.css";
import "../buttons/css/SubmitButton.css";
import Form3Rows from "../structuralComponents/Form3Rows";
import FieldLabelGroup from "../structuralComponents/FieldLabelGroup";
import CloseIcon from "../icons/CloseIcon";

type ModalProps = {
    modalIsOpen: boolean,
    closeModal: () => void,
    reloadStockItems: () => void,
    setSuccessMessage: (input: string) => void,
    itemToEdit: StockItemModel
}

function EditItemModal(props: ModalProps) {
    const [editedStockItem, setEditedStockItem] = useState<StockItemModel>(props.itemToEdit)
    useEffect(() => setEditedStockItem(props.itemToEdit), [props.itemToEdit])


    const saveNewStockitem = () => {
        axios.put("/stock/overview", editedStockItem)
            .catch((e) => console.error("PUT Error: " + e))
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
        >
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
                        </FieldLabelGroup>
                        <FieldLabelGroup>
                            <label htmlFor={"amount"}>Menge in <abbr title={"Kilogramm"}>kg</abbr></label>
                            <input value={editedStockItem.amountInStock} onChange={handleChange} placeholder={"0"}
                                   step={"0.1"} min={"0"} required
                                   type={"number"}
                                   id={"amount"} name={"amountInStock"}/>
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
                        <input className={"submit-button"} type={"submit"} value={"Änderungen speichern"}/>
                    </div>
                </form>
            </section>
        </Modal>
    );
}

export default EditItemModal;