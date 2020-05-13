import { Entity } from './entity.model';
import { Address } from './address.model';
import { Team } from './team.model';
import { Segment } from './segment.model';

export class Purchaser {
    id: number;
    nom: string;
    prenom: string;
    mail: string;
    entite: Entity;
    equipe : Team;
    segments : Segment[];
    adresse: Address;
    telephone: string;
}