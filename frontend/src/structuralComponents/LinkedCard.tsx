import React from 'react';
import {Link} from "react-router-dom";
import "./css/LinkedCard.css";

type linkedCardProps = {
    title: string,
    link: string,
    furtherInformation: string
}

function LinkedCard(props: linkedCardProps) {
    return (
        <Link className={"linked-card"} to={props.link} title={props.title}>
            <div>
                <h3>{props.title}</h3>
                <p>{props.furtherInformation}</p>
            </div>
        </Link>
    );
}

export default LinkedCard;