import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { User } from '../../model/user.model';
import { UrlConfig } from '../core/config/url-config';
import 'rxjs/add/operator/map';
import { Observable } from 'rxjs';

@Injectable()
export class UserService {

  constructor(private http: HttpClient) { }

  save(user: User){
    return this.http.post(UrlConfig.API_URL + "/api/users", user);
  }

  modifyPassword(user: User, password : String){
    const credentials = {mail: user.mail, password: password};
    return this.http.post(UrlConfig.API_URL + "/api/users/password" , credentials, {responseType: 'text'});
  }
  modifyPasswordWithMail(mail:String, password : String){
    const credentials = {mail: mail, password: password};
    return this.http.post(UrlConfig.API_URL + "/api/users/password" , credentials, {responseType: 'text'});
  }

  activateUser(token:string): Observable<any> {
    return this.http.get<any>(UrlConfig.API_URL + "/api/activate/" + token);
  }
}
