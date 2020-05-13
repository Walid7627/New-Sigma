import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpRequest, HttpParams } from '@angular/common/http';
import { UrlConfig } from '../core/config/url-config';
import { Provider } from '../../model/provider.model';
import { UserStorage } from '../core/userstorage/user.storage';

@Injectable()
export class ProfileProviderService {

  constructor(private http: HttpClient) { }

  get() {
    let formdata: FormData = new FormData();

    let user = JSON.parse(new UserStorage().getUser());

    formdata.append('mail', user.mail);

    const req = new HttpRequest('POST', UrlConfig.API_URL + '/api/providers/searchByMail', formdata, {
      reportProgress: true,
      responseType: 'json'
    });

    return this.http.request(req);
  }

  getByMail(mail) {
    let formdata: FormData = new FormData();

    formdata.append('mail', mail);

    const req = new HttpRequest('POST', UrlConfig.API_URL + '/api/providers/searchByMail', formdata, {
      reportProgress: true,
      responseType: 'json'
    });

    return this.http.request(req);
  }


  getDocument(path) {
    let formdata: FormData = new FormData();

    formdata.append('path', path);

    const req = new HttpRequest('POST', UrlConfig.API_URL + '/api/files/get', formdata, {
      responseType: "blob"
    });
  
    return this.http.request(req)
  }

  deleteDocument(path) {
    let formdata: FormData = new FormData();
    let user = JSON.parse(new UserStorage().getUser());


    formdata.append('path', path);
    formdata.append('user', user.mail)

    const req = new HttpRequest('POST', UrlConfig.API_URL + '/api/files/delete', formdata, {
      responseType: "json"
    });

    return this.http.request(req)
  }
  deleteDocumentByMail(path,mail) {
    let formdata: FormData = new FormData();
   


    formdata.append('path', path);
    formdata.append('user', mail)

    const req = new HttpRequest('POST', UrlConfig.API_URL + '/api/files/delete', formdata, {
      responseType: "json"
    });

    return this.http.request(req)
  }
  update(user) {

    const req = new HttpRequest('POST', UrlConfig.API_URL + '/api/providers/update', JSON.stringify(user), {
      responseType: 'json',
      headers: new HttpHeaders().set('Content-Type', 'application/json')
    });

    return this.http.request(req);
  }

  addContact(contact, user) {

    let params = new HttpParams().set('mail', user.mail);

    const req = new HttpRequest('POST', UrlConfig.API_URL + '/api/providers/contact/add', JSON.stringify(contact), {
      params: params,
      responseType: 'json',
      headers: new HttpHeaders().set('Content-Type', 'application/json')
    });

    return this.http.request(req);
  }

  removeContact(contactId, user) {

    let params = new HttpParams().set('mail', user.mail).set('contactId', contactId);

    const req = new HttpRequest('GET', UrlConfig.API_URL + '/api/providers/contact/remove', {
      params: params,
      responseType: 'json',
    });

    return this.http.request(req);
  }


}
