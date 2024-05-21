import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { WebApiService } from './web-api.service';

var apiUrl = "http://localhost:8080/";

var httpLink = {
  getAllTours: apiUrl + "api/v1/tours",
  getTourbyId: apiUrl + "api/v1/tours",
  deleteTourById: apiUrl + "api/v1/tours",
  updateTourbyId: apiUrl + "api/v1/tours",
  createTour: apiUrl + "api/v1/tours",
}

@Injectable({
  providedIn: 'root'
})

export class HttpProviderService {
  constructor(private webApiService: WebApiService) { }

  public getAllTours(): Observable<any> {
    return this.webApiService.get(httpLink.getAllTours);
  }
  public getTourbyId(id: number): Observable<any> {
    return this.webApiService.get(`${httpLink.getTourbyId}/${id}`);
  }
  public deleteTourById(id: number): Observable<any> {
    return this.webApiService.delete(`${httpLink.deleteTourById}/${id}`);
  }
  public updateTourbyId(id: number, model: any): Observable<any> {
    return this.webApiService.put(`${httpLink.updateTourbyId}/${id}`, model);
  }
  public createTour(model: any): Observable<any> {
    return this.webApiService.post(httpLink.createTour, model);
  }
}
