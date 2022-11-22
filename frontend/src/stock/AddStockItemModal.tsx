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
            <button onClick={() => props.closeModal()}>Schließen</button>
            <form>
                <input type={"text"}/>
            </form>
        </section>
            </Modal>
    );
}

export default AddStockItemModal;