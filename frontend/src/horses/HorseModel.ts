import {ConsumptionModel} from "./ConsumptionModel";

export type HorseModel = {
    id: string,
    name: string,
    owner: string,
    consumptionList: ConsumptionModel[]
}
