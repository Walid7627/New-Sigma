import {User, UserType} from "./user.model";
import {Address} from "./address.model";
import { Contact } from './contact.model';

export enum CompanyType {
  TYPE_MAISON_MERE="TYPE_MAISON_MERE",
  TYPE_FILIALE="TYPE_FILIALE",
  TYPE_SUCCURSALE="TYPE_SUCCURSALE"
}
export class Provider {
  id : number;
  nom: string;
  prenom: string;
  mail: string;
  adresse: Address;
  password: string;
  logo: string;
  nomSociete: string;
  codeAPE: string;
  codeCPV: string;
  raisonSociale: string;
  siteInstitutionnel: string;
  numSiret: string;
  description:string;
  typeEntreprise: CompanyType;
  telephone: string;
  mobile: string;
  fax: string;
  evaluation: string;
  adressePhysique: string;
  maisonMere: number;
  contacts : Contact[];
  constructor(){
    
  }
}
