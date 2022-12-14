import {HorseModel} from "../horses/HorseModel";

export type ClientModel = {
    id: string,
    name: string,
    ownsHorse: HorseModel[]
}
