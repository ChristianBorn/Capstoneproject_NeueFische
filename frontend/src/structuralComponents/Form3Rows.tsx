import React from 'react';
import "./css/Form3Rows.css";

type ParentParams = {
    children: React.ReactNode
}

function Form3Rows(props: ParentParams) {
    return (
        <div className={"form-3-rows"}>
            {props.children}
        </div>
    );
}

export default Form3Rows;
