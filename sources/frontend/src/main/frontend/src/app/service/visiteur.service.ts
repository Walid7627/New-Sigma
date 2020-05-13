import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams, HttpRequest } from '@angular/common/http';
import { UrlConfig } from '../core/config/url-config';

import 'rxjs/add/operator/map';
import { Visiteur } from '../../model/visiteur.model';
import { Observable } from 'rxjs/Observable';

@Injectable()
export class VisiteurService {

  allVisiteursUrl = UrlConfig.API_URL + "/api/visiteur";


  constructor(private http: HttpClient) { }

  save(visiteurs: Visiteur) {
    
    return this.http.post(this.allVisiteursUrl, JSON.stringify(visiteurs), {
      headers: new HttpHeaders().set('Content-Type', 'application/json')
    });
  }

  updateVisiteur(visiteur: Visiteur): Observable<Visiteur> {
    const headers = new HttpHeaders().set('Content-Type', 'application/json');
    console.log(JSON.stringify(visiteur));
    return this.http.post<Visiteur>(this.allVisiteursUrl + "/update", JSON.stringify(visiteur), { headers: headers });
  }

  getAllVisiteurs() {
    return this.http.get<Visiteur[]>(this.allVisiteursUrl);
  }

  getFreeVisiteurs() {
    return this.http.get<Visiteur[]>(this.allVisiteursUrl+'/with-any-entity');
  }
  
  deleteVisiteur(id: number): Observable<Visiteur> {
    const headers = new HttpHeaders().set('Content-Type', 'application/json');
    return this.http.get<Visiteur>(this.allVisiteursUrl + '/delete/' + id, { headers: headers });
  }

  
}