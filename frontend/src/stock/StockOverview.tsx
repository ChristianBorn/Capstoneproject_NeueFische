import React, {useEffect, useState} from 'react';
import axios from "axios";
import "./css/StockOverview.css";
import {StockItemModel} from "./StockItemModel";
import AddIcon from "./AddIcon";
import ClipLoader from "react-spinners/ClipLoader";

function StockOverview() {

    const [stockItems, setStockItems] = useState<StockItemModel[]>()

    const getAllStockItems = () => {
        axios.get("/lager/ueberblick")
            .then((response) => response.data)
            .catch((error) => console.error("Error while getting Stockitems:" + error))
            .then(data => setStockItems(data))
    }


    useEffect(() => {
        getAllStockItems()
    }, [])


    if (stockItems === undefined) {
        console.log("Cliploader")
        return <ClipLoader
            size={150}
            aria-label="Loading Spinner"
            data-testid="loader"
        />
    }
    return (
        <>
            {stockItems.length > 0 ?
                <>
                <div className={"stock-overview-table-wrapper"}>
                    <table>
                    <tbody>
                <tr>
                    <th>Name</th>
                    <th>Typ</th>
                    <th>Verfügbare Menge in Kilogramm</th>
                    <th>Preis pro Kilogramm</th>
                    <th>Täglicher Verbrauch</th>
                    <th>Aufgebraucht in</th>
                    <th>Aktionen</th>
                </tr>
                    {stockItems.map(singleItem => {
                        return <tr key={singleItem.id}>
                            <td>{singleItem.name}</td>
                            <td>{singleItem.type}</td>
                            <td>{singleItem.amountInStock}</td>
                            <td>{singleItem.pricePerKilo}</td>
                            <td>Lorem</td>
                            <td>Lorem</td>
                            <td>Lorem</td>
                        </tr>
                    })
                }
                    </tbody>
                </table>

                </div>
                <AddIcon/></>
            :
            <div>
                <p>Keine Items im Lager</p>
                <AddIcon/>
            </div>

            }
</>
    );
}

export default StockOverview;