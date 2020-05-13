import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders, HttpParams, HttpRequest} from '@angular/common/http';
import {Entity} from '../../model/entity.model';
import { FormGroup, FormControl, Validators } from "@angular/forms";
import { Observable } from 'rxjs';
import 'rxjs/add/operator/map';
import { EntitiesListComponent } from '../entities-list/entities-list.component';
import { catchError } from 'rxjs/operators';
import { UrlConfig } from '../core/config/url-config';
import { AdminEntity } from '../../model/admin-entity.model';
import { UserStorage } from '../core/userstorage/user.storage';

@Injectable()
export class EntityService {

  private serviceUrl = UrlConfig.API_URL + '/api/entities';
  //private serviceUrl = "../../assets/entities.json";

  constructor(private http: HttpClient) {
    
  }

  save(entity: Entity) {
    // let headers = new Headers({ 'Content-Type': 'application/json' });
    // let options = new RequestOptions({ headers: headers });

    return this.http.post(this.serviceUrl, JSON.stringify(entity), {
      headers: new HttpHeaders().set('Content-Type', 'application/json')
    });
  }

  getEntities()  {
    return this.http.get<Entity[]>(this.serviceUrl);
  }

  getMembers() {
    let formdata: FormData = new FormData();

    let user = JSON.parse(new UserStorage().getUser());

    formdata.append('mail', user.mail);

    const req = new HttpRequest('POST', this.serviceUrl + "/members", formdata, {
      reportProgress: true,
      responseType: 'json'
    });

    return this.http.request(req);
  }

  getTeams() {
    let formdata: FormData = new FormData();

    let user = JSON.parse(new UserStorage().getUser());

    formdata.append('mail', user.mail);

    const req = new HttpRequest('POST', this.serviceUrl + "/teams", formdata, {
      reportProgress: true,
      responseType: 'json'
    });

    return this.http.request(req);
  }

  updateEntity(entity: Entity): Observable<Entity> {
    const headers = new HttpHeaders().set('Content-Type', 'application/json');
    console.log(JSON.stringify(entity));
    return this.http.post<Entity>(this.serviceUrl + "/update", JSON.stringify(entity), { headers: headers });
  }

  deleteEntity(id: number): Observable<Entity> {
    const headers = new HttpHeaders().set('Content-Type', 'application/json');
    return this.http.get<Entity>(this.serviceUrl + '/delete/' + id, { headers: headers });
  }

  errorHandler(error:Response) {
    return Observable.throw(error||"SERVER_ERROR");
  }

  affectAdminToEntity(entity: Entity, id: number): Observable<Entity> {
    const headers = new HttpHeaders().set('Content-Type', 'application/json');
    console.log(JSON.stringify(entity));
    return this.http.get<Entity>(this.serviceUrl + "/admin/" + entity.id + "/" + id, { headers: headers });
  }

  getDocument() {
    
    const req = new HttpRequest('GET', this.serviceUrl + '/export', {
      responseType: "blob"
    });
  
    return this.http.request(req)
  }

}
