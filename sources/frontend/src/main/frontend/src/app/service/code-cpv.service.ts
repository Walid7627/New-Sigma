import { Injectable } from '@angular/core';
import { HttpClient, HttpRequest, HttpEvent } from '@angular/common/http';
import { UrlConfig } from '../core/config/url-config';

import 'rxjs/add/operator/map';
import { Observable } from 'rxjs';

@Injectable()
export class CodeCPVService {

  constructor(private http: HttpClient) { }

  getCodeCpv() : Observable<any>{
    return this.http.get(UrlConfig.API_URL + "/api/codecpv");
  }

}
