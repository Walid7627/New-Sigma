import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams, HttpRequest } from '@angular/common/http';
import { UrlConfig } from '../core/config/url-config';

import 'rxjs/add/operator/map';
import { AdminEntity } from '../../model/admin-entity.model';
import { Observable } from 'rxjs/Observable';

@Injectable()
export class AdminEntityService {

  allAdminsUrl = UrlConfig.API_URL + "/api/admins-entity";


  constructor(private http: HttpClient) { }

  save(admins: AdminEntity) {
    
    return this.http.post(this.allAdminsUrl, JSON.stringify(admins), {
      headers: new HttpHeaders().set('Content-Type', 'application/json')
    });
  }

  updateAdminEntity(admin: AdminEntity): Observable<AdminEntity> {
    const headers = new HttpHeaders().set('Content-Type', 'application/json');
    console.log(JSON.stringify(admin));
    return this.http.post<AdminEntity>(this.allAdminsUrl + "/update", JSON.stringify(admin), { headers: headers });
  }

  getAllAdmins() {
    return this.http.get<AdminEntity[]>(this.allAdminsUrl);
  }

  getFreeAdmins() {
    return this.http.get<AdminEntity[]>(this.allAdminsUrl+'/with-any-entity');
  }
  
  deleteAdmin(id: number): Observable<AdminEntity> {
    const headers = new HttpHeaders().set('Content-Type', 'application/json');
    return this.http.get<AdminEntity>(this.allAdminsUrl + '/delete/' + id, { headers: headers });
  }

  
}