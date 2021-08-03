import { IDrouss } from 'app/entities/drouss/drouss.model';

export interface IXassaide {
  id?: number;
  nom?: string;
  drousses?: IDrouss[] | null;
}

export class Xassaide implements IXassaide {
  constructor(public id?: number, public nom?: string, public drousses?: IDrouss[] | null) {}
}

export function getXassaideIdentifier(xassaide: IXassaide): number | undefined {
  return xassaide.id;
}
