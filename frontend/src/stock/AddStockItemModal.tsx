import React from 'react';
import Modal from 'react-modal';

type ModalProps = {
    modalIsOpen: boolean,
    closeModal: () => void,
}

function AddStockItemModal(props: ModalProps) {
    return (
        <Modal
            isOpen={props.modalIsOpen}
            // onAfterOpen={afterOpenModal}
            onRequestClose={props.closeModal}
            // style={customStyles}
            contentLabel="Example Modal"
        >
        <section >
            <button className={"modal-close-button"} onClick={() => props.closeModal()}>Schließen</button>
            <form onSubmit={event => console.log(event)}>

                <label htmlFor={"name"}>Name/Bezeichnung</label>
                <input required type={"text"} id={"name"} name={"name"}/>

                <label htmlFor={"price"}>Preis pro Kilogramm</label>
                <input placeholder={"0"} step={"0.1"} min={"0"} required type={"number"} id={"price"} name={"price"}/>

                <label htmlFor={"amount"}>Menge in Kilogramm</label>
                <input placeholder={"0"} step={"0.1"} min={"0"} required type={"number"} id={"amount"} name={"amount"}/>

                <label htmlFor={"type"}>Typ:</label>
                <select required id={"type"} name={"type"}>
                    <option disabled value={""}>Bitte auswählen</option>
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