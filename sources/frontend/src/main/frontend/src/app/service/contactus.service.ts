import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { UrlConfig } from '../core/config/url-config';

import 'rxjs/add/operator/map';

@Injectable()
export class ContactusService {

  constructor(private http: HttpClient) { }

  save(anonyme) {
    // let headers = new Headers({ 'Content-Type': 'application/json' });
    // let options = new RequestOptions({ headers: headers });

    return this.http.post(UrlConfig.API_URL + "/api/contacts", JSON.stringify(anonyme), {
      headers: new HttpHeaders().set('Content-Type', 'application/json'),
      responseType: 'text'
    });
  }

}
