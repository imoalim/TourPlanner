import { Injectable } from '@angular/core';
import { Observable, throwError } from 'rxjs';
import { map } from 'rxjs/operators';
import { catchError } from 'rxjs/internal/operators/catchError';
import { HttpHeaders, HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})

export class WebApiService {
  constructor(private httpClient: HttpClient) { }

  get(url: string): Observable<any> {
    return this.httpClient.get(url, { observe: 'response' });
  }

  post(url: string, model: any): Observable<any> {
    return this.httpClient.post(url, model, { observe: 'response' });
  }

  delete(url: string): Observable<any> {
    return this.httpClient.delete(url, { observe: 'response' });
  }

  put(url: string, model: any): Observable<any> {
    return this.httpClient.put(url, model, { observe: 'response' });
  }
}
