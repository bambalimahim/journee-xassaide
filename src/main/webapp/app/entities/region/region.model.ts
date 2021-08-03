import { ILieu } from 'app/entities/lieu/lieu.model';
import { IDaara } from 'app/entities/daara/daara.model';

export interface IRegion {
  id?: number;
  nom?: string;
  lieu?: ILieu | null;
  daaras?: IDaara[] | null;
}

export class Region implements IRegion {
  constructor(public id?: number, public nom?: string, public lieu?: ILieu | null, public daaras?: IDaara[] | null) {}
}

export function getRegionIdentifier(region: IRegion): number | undefined {
  return region.id;
}
