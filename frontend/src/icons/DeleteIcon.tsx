import React from 'react';
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faTrash} from "@fortawesome/free-solid-svg-icons";
import "./css/IconClickable.css"
import "./css/DeleteIcon.css"

type IconProps = {
    onClickAction: (id: string) => any,
    idToDelete: string,
}

function DeleteIcon(props: IconProps) {
    return (
        <span title={"Eintrag löschen"} onClick={() => props.onClickAction(props.idToDelete)}
              className={"clickable-icon delete-record-icon"}>
                    <FontAwesomeIcon icon={faTrash}/>
                            </span>

    );
}

export default DeleteIcon;
