import React, {useEffect, useState} from 'react';
import axios from "axios";
import "./css/StockOverview.css";
import {StockItemModel} from "./StockItemModel";
import AddIcon from "./AddIcon";
import ClipLoader from "react-spinners/ClipLoader";
import DeleteIcon from "../icons/DeleteIcon";
import AddStockItemModal from "./AddStockItemModal";

function StockOverview() {

    const [stockItems, setStockItems] = useState<StockItemModel[]>()
    const [modalIsOpen, setModalIsOpen] = useState<boolean>(false)
    const [successMessage, setSuccessMessage] = useState<string>()

    const getAllStockItems = () => {
        axios.get("/stock/overview")
            .then((response) => response.data)
            .catch((error) => console.error("Error while getting Stockitems:" + error))
            .then(data => setStockItems(data))
    }
    const openModal = () => {
        setModalIsOpen(true)
        setSuccessMessage("")
    }

    const closeModal = () => {
        setModalIsOpen(false)
    }


    useEffect(() => {
        getAllStockItems()
    }, [])


    if (stockItems === undefined) {
        return <ClipLoader
            size={150}
            aria-label="Loading Spinner"
            data-testid="loader"
        />
    }

    const deleteStockItem = (id: string) => {
        axios.delete("/stock/overview/" + id)
            .catch(error => console.error("DELETE Error: " + error))
            .then(() => alert("Eintrag gelöscht!"))
            .then(getAllStockItems)

    }

    return (
        <>
            <AddStockItemModal modalIsOpen={modalIsOpen}
                               closeModal={closeModal}
                               reloadStockItems={getAllStockItems}
                               setSuccessMessage={setSuccessMessage}/>
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
                                    <td><DeleteIcon onClickAction={deleteStockItem}
                                                    idToDelete={singleItem.id}/></td>
                                </tr>
                            })
                            }
                            </tbody>
                        </table>

                    </div>
                    {successMessage && <div className={"success-message"}>{successMessage}</div>}
                    <AddIcon openModal={openModal}/></>
                :
                <div>
                    <p>Keine Items im Lager</p>
                    <AddIcon openModal={openModal}/>
                </div>

            }
        </>
    );
}

export default StockOverview;