import { IXassaide } from 'app/entities/xassaide/xassaide.model';
import { ILieu } from 'app/entities/lieu/lieu.model';

export interface IDrouss {
  id?: number;
  nombre?: number;
  xassaide?: IXassaide | null;
  lieu?: ILieu | null;
}

export class Drouss implements IDrouss {
  constructor(public id?: number, public nombre?: number, public xassaide?: IXassaide | null, public lieu?: ILieu | null) {}
}

export function getDroussIdentifier(drouss: IDrouss): number | undefined {
  return drouss.id;
}
