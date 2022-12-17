import React from 'react';
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faCirclePlus} from "@fortawesome/free-solid-svg-icons";
import "./css/IconClickable.css"
import "./css/AddIcon.css"

type iconProps = {
    openModal: () => void
    title: string
}

function AddIcon(props: iconProps) {
    return (
        <span title={props.title} onClick={() => props.openModal()} className={"add-record-icon"}>
                    <FontAwesomeIcon icon={faCirclePlus}/>
        </span>
    );
}

export default AddIcon;
