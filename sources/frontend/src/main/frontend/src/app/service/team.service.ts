import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams, HttpRequest } from '@angular/common/http';
import { UrlConfig } from '../core/config/url-config';

import 'rxjs/add/operator/map';
import { Team } from '../../model/team.model';
import { Observable } from 'rxjs/Observable';
import {log} from "util";
import {Purchaser} from "../../model/purchaser.model";

@Injectable()
export class TeamService {

  allTeamUrl = UrlConfig.API_URL + "/api/team";

  constructor(private http: HttpClient) { }

  save(team: Team) {
    return this.http.post(this.allTeamUrl, JSON.stringify(team), {
      headers: new HttpHeaders().set('Content-Type', 'application/json')
    });
  }

  updateTeam(Team: Team, id: number) {
    const headers = new HttpHeaders().set('Content-Type', 'application/json');
    return this.http.post(this.allTeamUrl + "/update/" + id, JSON.stringify(Team), { headers: headers });
  }

  getAllPurchasers(id: number) {
    return this.http.get<Purchaser[]>(this.allTeamUrl + '/members/' + id);
  }

  getAllTeam() {
    return this.http.get<Team[]>(this.allTeamUrl);
  }

  deleteAdmin(id: number): Observable<Team> {
    const headers = new HttpHeaders().set('Content-Type', 'application/json');
    return this.http.get<Team>(this.allTeamUrl + '/delete/' + id, { headers: headers });
  }

  addPurchaser(acheteur, equipe) {
    let params = new HttpParams().set('equipe', equipe).set('acheteur', acheteur);

    const req = new HttpRequest('GET', UrlConfig.API_URL + '/api/team/acheteurs/add', {
      params: params,
      responseType: 'json',
    });

    return this.http.request(req);
  }


  removePurchaser(acheteur_id, equipe_id) {

    let params = new HttpParams().set('equipe', equipe_id).set('acheteur', acheteur_id);

    const req = new HttpRequest('GET', UrlConfig.API_URL + '/api/team/acheteurs/supprimer', {
      params: params,
      responseType: 'json',
    });

    return this.http.request(req);
  }

  getById(id) {
    let formdata: FormData = new FormData();

    formdata.append('id', id);

    const req = new HttpRequest('POST', UrlConfig.API_URL + '/api/team/searchById', formdata, {
      reportProgress: true,
      responseType: 'json'
    });

    return this.http.request(req);
  }

}
