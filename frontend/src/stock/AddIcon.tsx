import React from 'react';
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faCirclePlus} from "@fortawesome/free-solid-svg-icons";

type iconProps = {
    openModal: () => void
}

function AddIcon(props: iconProps) {
    return (
        <button onClick={() => props.openModal()}>
                        <span className={"add-record-icon"}>
                    <FontAwesomeIcon icon={faCirclePlus}/>
                            </span>
        </button>
    );
}

export default AddIcon;