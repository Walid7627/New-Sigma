import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams, HttpRequest } from '@angular/common/http';
import { UrlConfig } from '../core/config/url-config';

import 'rxjs/add/operator/map';
import { Team } from '../../model/team.model';
import { Observable } from 'rxjs/Observable';
import {log} from "util";
import {Purchaser} from "../../model/purchaser.model";
import { UserStorage } from '../core/userstorage/user.storage';

@Injectable()
export class TeamService {

  allTeamUrl = UrlConfig.API_URL + "/api/team";

  constructor(private http: HttpClient) { }

  add(team: Team) {

    let user = JSON.parse(new UserStorage().getUser());
    let mailTab = user.mail.split(".");
    return this.http.post(this.allTeamUrl + "/create/" + mailTab , JSON.stringify(team), {
      headers: new HttpHeaders().set('Content-Type', 'application/json')
    });
  }

  update(Team: Team, id: number) {
    const headers = new HttpHeaders().set('Content-Type', 'application/json');
    return this.http.post(this.allTeamUrl + "/update/" + id, JSON.stringify(Team), { headers: headers });
  }

  getAllTeam() {
    return this.http.get<any[]>(this.allTeamUrl);
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
