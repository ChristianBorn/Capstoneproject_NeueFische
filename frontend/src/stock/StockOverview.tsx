import React, {useEffect, useState} from 'react';
import axios from "axios";

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
            <p>Keine Items im Lager</p>}
        </div>
    );
}

export default StockOverview;