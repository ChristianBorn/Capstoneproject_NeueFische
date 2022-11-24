import axios from 'axios';
import React, {useState} from 'react';
import Modal from 'react-modal';
import {StockItemModel} from "./StockItemModel";
import "./css/AddStockItemModal.css";
import {faXmark} from "@fortawesome/free-solid-svg-icons";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";

type ModalProps = {
    modalIsOpen: boolean,
    closeModal: () => void,
    reloadStockItems: () => void,
    setSuccessMessage: (input: string) => void,
}

function AddStockItemModal(props: ModalProps) {
    const [newStockItem, setNewStockItem] = useState<StockItemModel>({
        id: "", name: "", amountInStock: 0, pricePerKilo: 0, type: ""
    })


    const saveNewStockitem = () => {
        axios.post("/stock/overview", newStockItem)
            .then(response => props.setSuccessMessage(response.data))
            .catch((e) => console.error("POST Error: " + e))
            .then(props.reloadStockItems)
            .then(props.closeModal)
            .then(() => console.log(newStockItem))
            .then(() => setNewStockItem({id: "", name: "", amountInStock: 0, pricePerKilo: 0, type: ""}))

    }

    const handleSubmit = (event: React.SyntheticEvent<HTMLFormElement>) => {
        event.preventDefault()
        saveNewStockitem()
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
        >
            <span className={"modal-close-button"} onClick={() => props.closeModal()}>
            <FontAwesomeIcon icon={faXmark}/>
            </span>
            <section>
                <h2>Neue Position dem Lager hinzufügen</h2>
                <form onSubmit={handleSubmit}>

                    <div className={"field-label-group"}>
                        <label htmlFor={"name"}>Name/Bezeichnung</label>
                        <input onChange={handleChange} required type={"text"} id={"name"} name={"name"}/>
                    </div>
                    <div className={"form-3-rows"}>
                        <div className={"field-label-group"}>
                            <label htmlFor={"price"}>Preis pro Kilogramm</label>
                            <input onChange={handleChange} placeholder={"0"} step={"0.1"} min={"0"} required
                                   type={"number"}
                                   id={"price"} name={"pricePerKilo"}/>
                        </div>
                        <div className={"field-label-group"}>
                            <label htmlFor={"amount"}>Menge in Kilogramm</label>
                            <input onChange={handleChange} placeholder={"0"} step={"0.1"} min={"0"} required
                                   type={"number"}
                                   id={"amount"} name={"amountInStock"}/>
                        </div>
                        <div className={"field-label-group"}>
                            <label htmlFor={"type"}>Typ</label>
                            <select onChange={handleChange} required id={"type"} name={"type"}>
                                <option value="" selected disabled hidden>Bitte auswählen</option>
                                <option value={"Futter"}>Futter</option>
                                <option value={"Einstreu"}>Einstreu</option>
                            </select>
                        </div>

                    </div>
                    <input className={"submit-button"} type={"submit"} value={"Einlagern"}/>
                </form>
            </section>
        </Modal>
    );
}

export default AddStockItemModal;