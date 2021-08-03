import { ILieu } from 'app/entities/lieu/lieu.model';
import { IMouride } from 'app/entities/mouride/mouride.model';

export interface ICuisine {
  id?: number;
  nom?: string;
  capacite?: number | null;
  lieu?: ILieu | null;
  mourides?: IMouride[] | null;
}

export class Cuisine implements ICuisine {
  constructor(
    public id?: number,
    public nom?: string,
    public capacite?: number | null,
    public lieu?: ILieu | null,
    public mourides?: IMouride[] | null
  ) {}
}

export function getCuisineIdentifier(cuisine: ICuisine): number | undefined {
  return cuisine.id;
}
