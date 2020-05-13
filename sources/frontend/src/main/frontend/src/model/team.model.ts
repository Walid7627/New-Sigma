import { Entity } from './entity.model';
import { Purchaser } from './purchaser.model';


export class Team {
  id: number;
  libelle: string;
  entite: number;
  responsable: number;
  membres: Array<number>;
}
