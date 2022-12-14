import {HorseModel} from "../horses/HorseModel";

export type ClientModel = {
    id: string,
    name: string,
    owns: HorseModel[],
    clientSince: string
}
