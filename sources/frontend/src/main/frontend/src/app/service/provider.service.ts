import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpRequest, HttpParams } from '@angular/common/http';
import { Provider } from '../../model/provider.model';
import { UrlConfig } from '../core/config/url-config';

import 'rxjs/add/operator/map';
import { Observable } from 'rxjs';

@Injectable()
export class ProviderService {

  allProvidersUrl = UrlConfig.API_URL + "/api/providers";
  allMotherCompanyUrl = UrlConfig.API_URL + "/api/providers/motherCompany";
  searchUrl = UrlConfig.API_URL + "/api/providers/searchByName";
  deleteUrl = UrlConfig.API_URL + "/api/providers/delete";
  updateUrl = UrlConfig.API_URL + "/api/providers/update";
  exportUrl = UrlConfig.API_URL + "/api/providers/export";
  lastProvidersUrl = UrlConfig.API_URL + "/api/providers/last";
  constructor(private http: HttpClient) { }

  save(provider: Provider) {
    // let headers = new Headers({ 'Content-Type': 'application/json' });
    // let options = new RequestOptions({ headers: headers });

    return this.http.post(UrlConfig.API_URL + "/api/providers", JSON.stringify(provider), {
      headers: new HttpHeaders().set('Content-Type', 'application/json')
    });
  }

  getAllProviders() {
    return this.http.get<Provider[]>(this.allProvidersUrl);
  }

  getAllMotherCompany() : Observable<Provider[]>{
    return this.http.get<Provider[]>(this.allMotherCompanyUrl);
  }

  getQualifiedProviders() {
    return this.http.get<Provider[]>(this.allProvidersUrl + "/qualified");
  }

  getProviderByName(name: string) {

    let requestParams = {
      nomSociete: name
    };

    return this.http.post(this.searchUrl, requestParams);
  }


  deleteProvider(id: number) {
    return this.http.get(this.deleteUrl + "/" + id);
  }

  updateProvider(provider: Provider) {
    return this.http.post(this.updateUrl, JSON.stringify(provider), {
      headers: new HttpHeaders().set('Content-Type', 'application/json')
    });
  }

  addQualification(qualif, provider: number) {
    return this.http.post(UrlConfig.API_URL + "/api/providers/qualif/" + provider , JSON.stringify(qualif), {
      headers: new HttpHeaders().set('Content-Type', 'application/json')
    });
  }

  removeQualification(id: number) {
    return this.http.get(this.allProvidersUrl + "/deleteQualif/" + id);
  }

  getQualification(id) {
    let formdata: FormData = new FormData();

    formdata.append('id', id);

    const req = new HttpRequest('POST', UrlConfig.API_URL + '/api/providers/getQualif', formdata, {
      reportProgress: true,
      responseType: 'json'
    });

    return this.http.request(req);
  }

  getQualifications(liste_id) {
    let formdata: FormData = new FormData();

    formdata.append('liste_id', liste_id);

    const req = new HttpRequest('POST', UrlConfig.API_URL + '/api/providers/getQualifs', formdata, {
      reportProgress: true,
      responseType: 'json'
    });

    return this.http.request(req);
  }
  getProviderByIdSegment(liste_id){
    let formdata: FormData = new FormData();

    formdata.append('liste_id', liste_id);

    const req = new HttpRequest('POST', UrlConfig.API_URL + '/api/providers/byIdSegment', formdata, {
      reportProgress: true,
      responseType: 'json'
    });

    return this.http.request(req);

  }

  getProvidersFile() {

    const req = new HttpRequest('GET', this.exportUrl, {
      responseType: "blob"
    });

    return this.http.request(req)
  }

  getLastProviders() {
    return this.http.get<Provider[]>(this.lastProvidersUrl);
  }

  getLogo(path) {
    let formdata: FormData = new FormData();

    formdata.append('path', path);

    const req = new HttpRequest('POST', UrlConfig.API_URL + '/api/files/logo', formdata, {
      responseType: "blob"
    });

    return this.http.request(req)
  }
  getProviderByCpv(cpv: string){
    return this.http.get<Provider[]>(this.allProvidersUrl +'/byCpv/'+cpv);
  }



}
