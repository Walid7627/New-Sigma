import { CPV } from './cpv.model';
import { APE } from './ape.model';
import { Provider } from '@angular/core';

export class Segment {
    id: number;
    libelle: string;
    codeCPV: string;
    acheteur: number;
    
    constructor(libelle : string, codeCPV : string, acheteur: number){
        this.libelle=libelle;
        this.codeCPV=codeCPV;
        this.acheteur=acheteur;
        
    }
    
}