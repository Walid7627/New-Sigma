import { Injectable } from '@angular/core';
import { HttpClient, HttpRequest, HttpEvent } from '@angular/common/http';
import { UrlConfig } from '../core/config/url-config';

import 'rxjs/add/operator/map';
import { APE } from '../../model/ape.model';

@Injectable()
export class CodeAPEService {

  constructor(private http: HttpClient) { }

  getCodeApe(){
    return this.http.get(UrlConfig.API_URL + "/api/codeape");
  }

  getCodeApeLibelle(code){
    return this.http.get<APE>(UrlConfig.API_URL + "/api/codeape/" + code);
  }

}
