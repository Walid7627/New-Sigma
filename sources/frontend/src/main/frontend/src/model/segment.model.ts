import { CPV } from './cpv.model';
import { APE } from './ape.model';
import { Provider } from '@angular/core';

export class Segment {
    id: number;
    libelle: string;
    codeCPV: string;
    codesCPV :Array<string>;
    //acheteur: number;
    acheteur:Array<string>;
    
    constructor(libelle : string, codeCPV : Array<string>, acheteur: Array<string>){
        this.libelle=libelle;
        this.codesCPV=codeCPV;
       // this.acheteur=acheteur;
        this.acheteur=acheteur;
        
    }
    

  /* constructor(){}*/
    
}