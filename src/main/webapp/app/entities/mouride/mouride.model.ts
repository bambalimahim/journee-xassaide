import { ICuisine } from 'app/entities/cuisine/cuisine.model';

export interface IMouride {
  id?: number;
  prenom?: string;
  nom?: string;
  email?: string | null;
  telephone?: string;
  matricule?: string | null;
  cuisine?: ICuisine | null;
}

export class Mouride implements IMouride {
  constructor(
    public id?: number,
    public prenom?: string,
    public nom?: string,
    public email?: string | null,
    public telephone?: string,
    public matricule?: string | null,
    public cuisine?: ICuisine | null
  ) {}
}

export function getMourideIdentifier(mouride: IMouride): number | undefined {
  return mouride.id;
}
