import React, {useEffect, useState} from 'react';
import axios from "axios";
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faCirclePlus } from '@fortawesome/free-solid-svg-icons';
import "./css/StockOverview.css";

function StockOverview() {

    const [stockItems, setStockItems] = useState<[]>([])

    const getAllStockItems = () => {
        axios.get("/lager/ueberblick")
            .then((response) => response.data)
            .catch((error) => console.error("Error while getting Stockitems:" + error))
            .then(data => setStockItems(data))
    }

    useEffect(() => {
        getAllStockItems()
    }, [])

    return (
        <div>
            {stockItems.length > 0 ? stockItems.map(singleItem => <div>{singleItem}</div>)
            :
            <div>
                <p>Keine Items im Lager</p>

                <a  href={"/lager/uebersicht"}>
                        <span className={"add-record-icon"}>
                    <FontAwesomeIcon icon={faCirclePlus} />
                            </span>
                    </a>

            </div>

            }
        </div>
    );
}

export default StockOverview;