import React, {ChangeEvent, useEffect, useState} from 'react';
import Modal from 'react-modal';
import Select from 'react-select';
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
    const [errorMessages, setErrorMessages] = useState({
        dailyConsumption: ""
    })
    const [selectedHorse, setSelectedHorse] = useState<HorseModel>(props.selectedHorse)
    const [consumptionSelectList, setConsumptionSelectList] = useState<{}[]>([])
    const [consumptionToAdd, setConsumptionToAdd] = useState<ConsumptionModel>(
        {id: "", name: "", dailyConsumption: 0.0}
    )
    useEffect(() => {
        setErrorMessages({dailyConsumption: ""})
    }, [props.closeModal])

    useEffect(() => {
        const newConsumptionSelectList: {}[] = []
        const existingConsumptionItemIds = props.selectedHorse.consumptionList.map(item => item.id)
        setSelectedHorse(props.selectedHorse)

        props.stockItemList.forEach(stockItem => {
            if (!existingConsumptionItemIds.includes(stockItem.id)) {
                newConsumptionSelectList.push(
                    {"label": stockItem.name, "value": stockItem.id}
                )
            }
        })
        setConsumptionSelectList(newConsumptionSelectList)

    }, [props.selectedHorse, props.stockItemList])

    const handleSubmit = (event: React.SyntheticEvent<HTMLFormElement>) => {
        event.preventDefault()
        selectedHorse.consumptionList.push(consumptionToAdd)
        axios.put("/horses/", selectedHorse)
            .catch((e) => {
                if (e.response.status === 400) {
                    setErrorMessages({
                        ...errorMessages,
                        [e.response.data.fieldName]: e.response.data.errorMessage
                    })
                }
                console.error("POST Error: " + e)
                selectedHorse.consumptionList.splice(selectedHorse.consumptionList.indexOf(consumptionToAdd), 1)
                throw e
            })
            .then(props.reloadHorses)
            .then(props.closeModal)
            .then(() => setSelectedHorse(props.selectedHorse))
            .then(() => props.setSuccessMessage("Eintrag erfolgreich geändert"))
    }

    const handleChange = (event: ChangeEvent<HTMLInputElement>) => {
        setConsumptionToAdd({
            ...consumptionToAdd,
            dailyConsumption: parseFloat(event.target.value)
        })
    }
    const handleSelectChange = (event: any) => {
        setConsumptionToAdd({
            ...consumptionToAdd,
            id: event.value,
            name: event.label
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
                        <label htmlFor={"stockitems"}>Typ</label>
                        <Select options={consumptionSelectList} onChange={handleSelectChange} required id={"stockitems"}
                                name={"stockitems"}/>
                    </FieldLabelGroup>

                    <FieldLabelGroup>
                        <label htmlFor={"dailyConsumption"}>Täglicher Verbrauch in <abbr
                            title={"Kilogramm"}>kg</abbr></label>
                        <input onChange={handleChange} placeholder={"0"}
                               step={"0.1"} min={"0"} required
                               type={"number"}
                               id={"dailyConsumption"} name={"dailyConsumption"}/>
                        {errorMessages.dailyConsumption &&
                            <div className={"message-container"}><p
                                className={"error-message"}>{errorMessages.dailyConsumption}</p></div>}
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