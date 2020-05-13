import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams, HttpRequest } from '@angular/common/http';
import { UrlConfig } from '../core/config/url-config';

import 'rxjs/add/operator/map';
import { Segment } from '../../model/segment.model';
import { Observable } from 'rxjs/Observable';

@Injectable()
export class SegmentService {

  allSegmentUrl = UrlConfig.API_URL + "/api/segment";
  createSegmentUrl = this.allSegmentUrl +"/create";
  getSegmentUrl = this.allSegmentUrl+"/list";
  deleteUrl = UrlConfig.API_URL + "/api/segment/delete"
  updateUrl= this.allSegmentUrl+"/update";


  constructor(private http: HttpClient) { }

  save(segment: Segment) {
    console.log('test save : segmentservice',segment);
    return this.http.post(this.createSegmentUrl, JSON.stringify(segment), {
      headers: new HttpHeaders().set('Content-Type', 'application/json')
    });
  }

  

  updateSegment(Segment: Segment ): Observable<Segment> {
    const headers = new HttpHeaders().set('Content-Type', 'application/json');
    console.log(JSON.stringify(Segment));
    return this.http.post<Segment>(this.updateUrl, JSON.stringify(Segment), { headers: headers });
  }

  getAllSegment() {
    return this.http.get<Segment[]>(this.getSegmentUrl);
  }
  
  deleteSegment(id: number): Observable<Segment>{
    const headers = new HttpHeaders().set('Content-Type', 'application/json');
    return this.http.get<Segment>(this.allSegmentUrl+ "/delete/" + id, { headers: headers });
  }

  
}