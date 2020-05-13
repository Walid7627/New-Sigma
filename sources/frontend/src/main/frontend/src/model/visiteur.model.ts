import { Entity } from './entity.model';
import { Address } from './address.model';

export class Visiteur {
    id: number;
    nom: string;
    prenom: string;
    mail: string;
    entite: Entity;
    adresse: Address;
    telephone: string;
}