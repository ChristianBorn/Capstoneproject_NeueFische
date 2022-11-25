import axios from 'axios';
import React from 'react';
import Modal from 'react-modal';
import "./css/AddStockItemModal.css";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faXmark} from "@fortawesome/free-solid-svg-icons";

type ModalProps = {
    modalIsOpen: boolean,
    closeModal: () => void,
    reloadStockItems: () => void,
    setSuccessMessage: (input: string) => void,
    idToDelete: string
}

function DeleteItemModal(props: ModalProps) {


    const deleteStockItem = (id: string) => {
        axios.delete("/stock/overview/" + id)
            .catch(error => console.error("DELETE Error: " + error))
            .then(() => alert("Eintrag gelöscht!"))
            .then(props.reloadStockItems)
            .then(() => props.setSuccessMessage("Eintrag erfolgreich gelöscht"))
            .then(props.closeModal)
    }
    return (
        <Modal
            isOpen={props.modalIsOpen}
            contentLabel="Delete Modal"
        >
            <span><p>Soll der Eintrag wirklich gelöscht werden?</p></span>
            <button onClick={() => deleteStockItem(props.idToDelete)}>Eintrag löschen</button>
            <button onClick={() => props.closeModal()}>Abbrechen</button>
            <span className={"modal-close-button"} onClick={() => props.closeModal()}>
            <FontAwesomeIcon icon={faXmark}/>
            </span>

        </Modal>
    );
}

export default DeleteItemModal;