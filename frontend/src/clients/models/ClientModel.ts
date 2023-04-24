import {HorseModel} from "../../horses/models/HorseModel";

export type ClientModel = {
    id: string,
    name: string,
    ownsHorse: HorseModel[]
}
