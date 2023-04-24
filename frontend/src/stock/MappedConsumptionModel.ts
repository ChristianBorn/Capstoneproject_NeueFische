import {AggregatedConsumptionModel} from "./models/AggregatedConsumptionModel";

export type MappedConsumptionModel = {
    [id: string]: AggregatedConsumptionModel;
}
