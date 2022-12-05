import React, {useState} from 'react';
import Modal from 'react-modal';
import {HorseModel} from "./HorseModel";
import {StockItemModel} from "../stock/StockItemModel";
import CloseIcon from "../icons/CloseIcon";
import FieldLabelGroup from "../structuralComponents/FieldLabelGroup";
import axios from "axios";
import {ConsumptionModel} from "./ConsumptionModel";

type ModalProps = {
    modalIsOpen: boolean,
    closeModal: () => void,
    stockItemList: StockItemModel[],
    selectedHorse: HorseModel,
    reloadHorses: () => void,
    setSuccessMessage: (input: string) => void,
}

function AddConsumptionModal(props: ModalProps) {
    const [selectedHorse, setSelectedHorse] = useState<HorseModel>(props.selectedHorse)
    const [consumptionToAdd, setConsumptionToAdd] = useState<ConsumptionModel>(
        {id: "", name: "", dailyConsumption: 0}
    )

    const handleSubmit = (event: React.SyntheticEvent<HTMLFormElement>) => {
        event.preventDefault()
        axios.put("/horses/", selectedHorse.consumption.push(consumptionToAdd))
            .catch((e) => console.error("PUT Error: " + e))
            .then(props.reloadHorses)
            .then(props.closeModal)
            .then(() => setSelectedHorse(props.selectedHorse))
            .then(() => props.setSuccessMessage("Eintrag erfolgreich geändert"))
    }

    const handleChange = (event: any) => {
        setConsumptionToAdd(event.target)
        const name = event.target.name;
        const value = event.target.value;
        setConsumptionToAdd({
            ...consumptionToAdd,
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
                <h2>Neuen Verbrauch anlegen</h2>
                <form onSubmit={handleSubmit}>

                    <FieldLabelGroup>
                        <label htmlFor={"dailyConsumptipn"}>Täglicher Verbrauch in <abbr
                            title={"Kilogramm"}>kg</abbr></label>
                        <input onChange={handleChange} placeholder={"0"} step={"0.1"} min={"0"} required
                               type={"number"}
                               id={"dailyConsumptipn"} name={"dailyConsumptipn"}/>
                    </FieldLabelGroup>

                    <div className={"button-group"}>
                        <button className={"submit-button"} type={"submit"}>Speichern</button>
                        <button className={"abort-button"} onClick={props.closeModal}>Abbrechen</button>
                    </div>
                </form>
            </section>

        </Modal>
    );
}

export default AddConsumptionModal;