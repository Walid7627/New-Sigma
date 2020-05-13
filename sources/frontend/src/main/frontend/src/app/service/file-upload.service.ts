import { Injectable } from '@angular/core';
import { HttpClient, HttpRequest, HttpEvent } from '@angular/common/http';
import { UrlConfig } from '../core/config/url-config';

@Injectable()
export class FileUploadService {
  constructor(private http: HttpClient) { }

  upload(file: File, user: string, type: string) {
    let formdata: FormData = new FormData();

    formdata.append('file', file);
    formdata.append('user', user);
    formdata.append('type', type);

    const req = new HttpRequest('POST', UrlConfig.API_URL + '/api/files', formdata, {
      reportProgress: true,
      responseType: 'json'
    });

    return this.http.request(req);
  }
}
