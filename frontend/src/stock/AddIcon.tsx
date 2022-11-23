import React from 'react';
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faCirclePlus} from "@fortawesome/free-solid-svg-icons";

type iconProps = {
    openModal: () => void
}

function AddIcon(props: iconProps) {
    return (
                        <span title={"Neuen Eintrag hinzufügen"} onClick={() => props.openModal()} className={"add-record-icon"}>
                    <FontAwesomeIcon icon={faCirclePlus}/>
                            </span>

    );
}

export default AddIcon;