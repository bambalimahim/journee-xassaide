import { IDaara } from 'app/entities/daara/daara.model';

export interface IKourel {
  id?: number;
  nom?: string;
  daara?: IDaara | null;
}

export class Kourel implements IKourel {
  constructor(public id?: number, public nom?: string, public daara?: IDaara | null) {}
}

export function getKourelIdentifier(kourel: IKourel): number | undefined {
  return kourel.id;
}
