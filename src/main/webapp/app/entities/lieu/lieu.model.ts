import { IDrouss } from 'app/entities/drouss/drouss.model';

export interface ILieu {
  id?: number;
  nom?: string;
  drousses?: IDrouss[] | null;
}

export class Lieu implements ILieu {
  constructor(public id?: number, public nom?: string, public drousses?: IDrouss[] | null) {}
}

export function getLieuIdentifier(lieu: ILieu): number | undefined {
  return lieu.id;
}
