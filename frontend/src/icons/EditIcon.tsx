import React from 'react';
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faPen} from "@fortawesome/free-solid-svg-icons";
import {StockItemModel} from "../stock/models/StockItemModel";
import "./css/IconClickable.css"
import "./css/EditIcon.css"

type IconProps = {
    onClickAction: (itemToEdit: StockItemModel) => any,
    itemToEdit: any,
}

function EditIcon(props: IconProps) {
    return (
        <span title={"Eintrag bearbeiten"} onClick={() => props.onClickAction(props.itemToEdit)}
              className={"clickable-icon edit-record-icon"}>
                    <FontAwesomeIcon icon={faPen}/>
        </span>
    );
}

export default EditIcon;
