import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams, HttpRequest } from '@angular/common/http';
import { UrlConfig } from '../core/config/url-config';

import 'rxjs/add/operator/map';
import { Purchaser } from '../../model/purchaser.model';
import { Observable } from 'rxjs/Observable';
import { UserStorage } from '../core/userstorage/user.storage';
import { AdminEntity } from '../../model/admin-entity.model';
import { Entity } from '../../model/entity.model';

@Injectable()
export class PurchaserService {

  allPurchaserUrl = UrlConfig.API_URL + "/api/purchasers";


  constructor(private http: HttpClient) { }

  save(purchaser: Purchaser) {
    let user = JSON.parse(new UserStorage().getUser());
    console.log(user.mail);
    let entite = new Entity();
    entite.administrateur = new AdminEntity();
    entite.administrateur.mail = user.mail;
    purchaser.entite = entite;
    return this.http.post(this.allPurchaserUrl , JSON.stringify(purchaser), {
      headers: new HttpHeaders().set('Content-Type', 'application/json')
    });
  }

  updatePurchaser(purchaser: Purchaser): Observable<Purchaser> {
    const headers = new HttpHeaders().set('Content-Type', 'application/json');
    console.log(JSON.stringify(purchaser));
    return this.http.post<Purchaser>(this.allPurchaserUrl + "/update", JSON.stringify(purchaser), { headers: headers });
  }

  getAllPurchaser() {
    return this.http.get<Purchaser[]>(this.allPurchaserUrl);
  }

  getFreePurchaser() {
    return this.http.get<Purchaser[]>(this.allPurchaserUrl+'/with-no-team');
  }
  
  deleteAdmin(id: number): Observable<Purchaser> {
    const headers = new HttpHeaders().set('Content-Type', 'application/json');
    return this.http.get<Purchaser>(this.allPurchaserUrl + '/delete/' + id, { headers: headers });
  }

  
}
