import React from 'react';
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faCirclePlus} from "@fortawesome/free-solid-svg-icons";
import "./css/IconClickable.css"
import "./css/AddIcon.css"
import {HorseModel} from "../horses/models/HorseModel";

type iconProps = {
    onClickAction: (addTo: HorseModel) => any
    title: string
    addTo: any
}

function AddToIcon(props: iconProps) {
    return (
        <span title={props.title} onClick={() => props.onClickAction(props.addTo)}
              className={"clickable-icon add-record-icon"}>
                    <FontAwesomeIcon icon={faCirclePlus}/>
        </span>
    );
}

export default AddToIcon;
