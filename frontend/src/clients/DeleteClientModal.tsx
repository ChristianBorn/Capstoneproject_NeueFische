import axios from 'axios';
import React from 'react';
import Modal from 'react-modal';
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faXmark} from "@fortawesome/free-solid-svg-icons";
import CloseIcon from "../icons/CloseIcon";
import "../index/css/DeleteStockItemModal.css";
import "../buttons/css/SubmitButton.css";
import "../buttons/css/AbortButton.css";

type ModalProps = {
    modalIsOpen: boolean,
    closeModal: () => void,
    reloadClients: () => void,
    reloadHorses: () => void,
    setSuccessMessage: (input: string) => void,
    idToDelete: string
}

function DeleteClientModal(props: ModalProps) {


    const deleteClient = (id: string) => {
        axios.delete("/clients/" + id)
            .catch(error => console.error("DELETE Error: " + error))
            .then(props.reloadClients)
            .then(props.reloadHorses)
            .then(props.closeModal)
            .then(() => props.setSuccessMessage("Eintrag erfolgreich gelöscht"))
    }
    return (
        <Modal
            isOpen={props.modalIsOpen}
            contentLabel="Delete Modal"
            ariaHideApp={false}
            onRequestClose={props.closeModal}>
            <CloseIcon closeModal={props.closeModal}/>
            <span><p>Soll der Eintrag wirklich gelöscht werden?</p></span>
            <div className={"button-group"}>
                <button className={"abort-button"} onClick={() => props.closeModal()}>Abbrechen</button>
                <button className={"submit-button"} onClick={() => deleteClient(props.idToDelete)}>Eintrag löschen
                </button>
            </div>
            <span className={"modal-close-button"} onClick={() => props.closeModal()}>
                <FontAwesomeIcon icon={faXmark}/>
            </span>
        </Modal>
    );
}

export default DeleteClientModal;
