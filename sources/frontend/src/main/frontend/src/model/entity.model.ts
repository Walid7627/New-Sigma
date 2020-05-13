import { Address } from './address.model';
import { APE } from './ape.model';
import { AdminEntity } from './admin-entity.model';
import { CompanyType } from './provider.model';

export class Entity {

  id : number;
	numSiret : String;
	logo: String;
	nomSociete: String;
	codeAPE : String;
	codeCPV : String;
	raisonSociale : String;
	typeEntreprise: CompanyType;
	maisonMere: String;
	siteInstitutionnel : String;
	description : String;
	mail : String;
	telephone : String;
	fax : String;
	adresse : Address;
	administrateur: AdminEntity;
  constructor() {
   
  }

}
