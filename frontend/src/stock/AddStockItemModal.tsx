import axios from 'axios';
import React, {useState} from 'react';
import Modal from 'react-modal';
import {StockItemModel} from "./StockItemModel";
import "./css/AddStockItemModal.css";

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
        axios.post("/lager/ueberblick", newStockItem)
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

    // const handleSelectChange = (event: ChangeEvent<HTMLSelectElement>) => {
    //     const name = event.target.name;
    //     const value = event.target.value;
    //     setNewStockItem({
    //         ...newStockItem,
    //         [name]: value
    //     })
    // }

    return (
        <Modal
            isOpen={props.modalIsOpen}
            // onRequestClose={props.closeModal}
            contentLabel="Example Modal"
        >
            <button className={"modal-close-button"} onClick={() => props.closeModal()}>Schließen</button>
            <section>
                <h2>Neue Position dem Lager hinzufügen</h2>
                <form onSubmit={handleSubmit}>

                    <label htmlFor={"name"}>Name/Bezeichnung</label>
                    <input onChange={handleChange} required type={"text"} id={"name"} name={"name"}/>

                    <label htmlFor={"price"}>Preis pro Kilogramm</label>
                    <input onChange={handleChange} placeholder={"0"} step={"0.1"} min={"0"} required type={"number"}
                           id={"price"} name={"pricePerKilo"}/>

                    <label htmlFor={"amount"}>Menge in Kilogramm</label>
                    <input onChange={handleChange} placeholder={"0"} step={"0.1"} min={"0"} required type={"number"}
                           id={"amount"} name={"amountInStock"}/>

                    <label htmlFor={"type"}>Typ:</label>
                    <select onChange={handleChange} required id={"type"} name={"type"}>
                        <option value="" selected disabled hidden>Bitte auswählen</option>
                        <option value={"Futter"}>Futter</option>
                        <option value={"Einstreu"}>Einstreu</option>
                    </select>
                    <input type={"submit"} value={"Einlagern"}/>

                </form>
            </section>
        </Modal>
    );
}

export default AddStockItemModal;