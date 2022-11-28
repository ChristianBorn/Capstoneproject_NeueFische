import React from 'react';
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faXmark} from "@fortawesome/free-solid-svg-icons";
import "./css/CloseIcon.css"

type iconProps = {
    closeModal: () => void
}

function CloseIcon(props: iconProps) {
    return (
        <span className={"modal-close-button"} onClick={() => props.closeModal()}>
            <FontAwesomeIcon icon={faXmark}/>
            </span>

    );
}

export default CloseIcon;
