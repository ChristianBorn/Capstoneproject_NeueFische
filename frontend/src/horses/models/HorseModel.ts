import {ConsumptionModel} from "./ConsumptionModel";
import {ClientModel} from "../../clients/models/ClientModel";

export type HorseModel = {
    id: string,
    name: string,
    owner: ClientModel,
    consumptionList: ConsumptionModel[]
}
