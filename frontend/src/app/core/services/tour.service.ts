import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import { Observable } from 'rxjs';
import { Tour } from 'src/app/models/tour.model';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class TourService {
  private apiServerUrl = environment.apiBaseUrl;

  constructor(private http: HttpClient) { }

  public getAllTours(): Observable<Tour[]>{
    return this.http.get<Tour[]>(`${this.apiServerUrl}/api/v1/tours`)
  }
  public getTour(tourId:number): Observable<Tour>{
    return this.http.get<Tour>(`${this.apiServerUrl}/api/v1/tours/${tourId}`)
  }
  public addTour(tour:Tour): Observable<Tour>{
    return this.http.post<Tour>('${this.apiServerUrl}/api/v1/tours',tour)
  }
  public removeTour(tourId:number): Observable<void>{
    return this.http.delete<void>(`${this.apiServerUrl}/api/v1/tours/${tourId}`)
  }
  public updateTour(tourId:number, tour:Tour): Observable<Tour>{
    return this.http.put<Tour>(`${this.apiServerUrl}/api/v1/tours/${tourId}`, tour)
  }
}
