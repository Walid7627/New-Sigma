import { Address } from './address.model';

export class Contact {
    id : number;
    nom: string;
    prenom: string;
    mail: string;
    telephone:string;
    mobile: string;
    nomSociete:string;
    numSiret:string;
    fax: string;
    adresse: Address;
    adressePhysique: string;
}