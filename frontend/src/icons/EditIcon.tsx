import React from 'react';
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faPen} from "@fortawesome/free-solid-svg-icons";
import "./css/IconClickable.css"
import "./css/EditIcon.css"

type IconProps = {
    onClickAction: (id: string) => any,
    idToEdit: string,
}

function EditIcon(props: IconProps) {
    return (
        <span title={"Eintrag bearbeiten"} onClick={() => props.onClickAction(props.idToEdit)}
              className={"clickable-icon edit-record-icon"}>
                    <FontAwesomeIcon icon={faPen}/>
                            </span>

    );
}

export default EditIcon;