import axios from 'axios';
import React from 'react';
import Modal from 'react-modal';
import "./css/AddStockItemModal.css";

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
            <button onClick={() => deleteStockItem(props.idToDelete)}>Eintrag löschen</button>
            <button className={"modal-close-button"} onClick={() => props.closeModal()}>Schließen</button>

        </Modal>
    );
}

export default DeleteItemModal;