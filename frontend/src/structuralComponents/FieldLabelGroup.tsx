import React from 'react';
import "./css/FieldLabelGroup.css";

type ParentParams = {
    children: React.ReactNode
}

function FieldLabelGroup(props: ParentParams) {
    return (
        <div className={"field-label-group"}>
            {props.children}
        </div>
    );
}

export default FieldLabelGroup;
