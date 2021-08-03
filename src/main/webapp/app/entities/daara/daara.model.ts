import { IKourel } from 'app/entities/kourel/kourel.model';
import { IRegion } from 'app/entities/region/region.model';

export interface IDaara {
  id?: number;
  nom?: string;
  kourels?: IKourel[] | null;
  region?: IRegion | null;
}

export class Daara implements IDaara {
  constructor(public id?: number, public nom?: string, public kourels?: IKourel[] | null, public region?: IRegion | null) {}
}

export function getDaaraIdentifier(daara: IDaara): number | undefined {
  return daara.id;
}
