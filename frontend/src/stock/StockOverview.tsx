import React, {useEffect, useState} from 'react';
import axios from "axios";
import {StockItemModel} from "./StockItemModel";
import AddIcon from "../icons/AddIcon";
import ClipLoader from "react-spinners/ClipLoader";
import DeleteIcon from "../icons/DeleteIcon";
import AddItemModal from "./AddStockItemModal";
import DeleteItemModal from "./DeleteItemModal";
import EditIcon from "../icons/EditIcon";
import EditItemModal from "./EditItemModal";

function StockOverview() {

    const [stockItems, setStockItems] = useState<StockItemModel[]>()
    const [addModalIsOpen, setAddModalIsOpen] = useState<boolean>(false)
    const [deleteModalIsOpen, setDeleteModalIsOpen] = useState<boolean>(false)
    const [editModalIsOpen, setEditModalIsOpen] = useState<boolean>(false)
    const [successMessage, setSuccessMessage] = useState<string>()
    const [idToDelete, setIdToDelete] = useState<string>("")
    const [itemToEdit, setItemToEdit] = useState<StockItemModel>({
        id: "", name: "", amountInStock: 0, pricePerKilo: 0, type: ""
    })

    const getAllStockItems = () => {
        axios.get("/stock/overview")
            .then((response) => response.data)
            .catch((error) => console.error("Error while getting Stockitems:" + error))
            .then(data => setStockItems(data))
    }
    const openAddModal = () => {
        setAddModalIsOpen(true)
        setSuccessMessage("")
    }
    const openDeleteModal = (id: string) => {
        setDeleteModalIsOpen(true)
        setIdToDelete(id)
        setSuccessMessage("")
    }
    const openEditModal = (itemToEdit: StockItemModel) => {
        setEditModalIsOpen(true)
        setItemToEdit(itemToEdit)
        setSuccessMessage("")
    }

    const closeModal = () => {
        setAddModalIsOpen(false)
        setDeleteModalIsOpen(false)
        setEditModalIsOpen(false)
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



    return (
        <>

            <AddItemModal modalIsOpen={addModalIsOpen}
                          closeModal={closeModal}
                          reloadStockItems={getAllStockItems}
                          setSuccessMessage={setSuccessMessage}/>
            <DeleteItemModal modalIsOpen={deleteModalIsOpen}
                             closeModal={closeModal}
                             reloadStockItems={getAllStockItems}
                             setSuccessMessage={setSuccessMessage} idToDelete={idToDelete}/>
            <EditItemModal modalIsOpen={editModalIsOpen}
                           closeModal={closeModal}
                           reloadStockItems={getAllStockItems}
                           setSuccessMessage={setSuccessMessage} itemToEdit={itemToEdit}/>
            {stockItems.length > 0 ?
                <>
                    <div className={"overview-table-wrapper"}>
                        <table>

                            <thead>
                            <tr>
                                <th>Name</th>
                                <th>Typ</th>
                                <th>Verfügbare Menge in <abbr title={"Kilogramm"}>kg</abbr></th>
                                <th>Preis pro <abbr title={"Kilogramm"}>kg</abbr></th>
                                <th>Täglicher Verbrauch</th>
                                <th>Aufgebraucht in</th>
                                <th>Aktionen</th>
                            </tr>
                            </thead>
                            <tbody>
                            {stockItems.map(singleItem => {
                                return <tr key={singleItem.id}>
                                    <td>{singleItem.name}</td>
                                    <td>{singleItem.type}</td>
                                    <td>{singleItem.amountInStock}</td>
                                    <td>{singleItem.pricePerKilo}</td>
                                    <td>Lorem</td>
                                    <td>Lorem</td>
                                    <td>
                                        <div className={"action-cell"}>
                                            <EditIcon onClickAction={openEditModal}
                                                      itemToEdit={singleItem}/>
                                            <DeleteIcon onClickAction={openDeleteModal}
                                                        idToDelete={singleItem.id}/>
                                        </div>
                                    </td>
                                </tr>
                            })
                            }
                            </tbody>
                        </table>

                    </div>
                    {successMessage && <div className={"success-message"}>{successMessage}</div>}
                    <AddIcon openModal={openAddModal}/></>
                :
                <div>
                    {successMessage && <div className={"success-message"}>{successMessage}</div>}
                    <p>Keine Items im Lager</p>
                    <AddIcon openModal={openAddModal}/>
                </div>

            }
        </>
    );
}

export default StockOverview;
