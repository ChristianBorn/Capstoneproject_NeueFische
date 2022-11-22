import React from 'react';
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faCirclePlus} from "@fortawesome/free-solid-svg-icons";

function AddIcon() {
    return (
        <a href={"/lager/ueberblick"}>
                        <span className={"add-record-icon"}>
                    <FontAwesomeIcon icon={faCirclePlus}/>
                            </span>
        </a>
    );
}

export default AddIcon;