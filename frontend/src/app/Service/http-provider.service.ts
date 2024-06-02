import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { WebApiService } from './web-api.service';

var apiUrl = "http://localhost:8080/";

var httpLink = {
  tour : {
    getAll: apiUrl + "api/v1/tours",
    getById: apiUrl + "api/v1/tours",
    deleteById: apiUrl + "api/v1/tours",
    updateById: apiUrl + "api/v1/tours",
    create: apiUrl + "api/v1/tours",
    getReport: (id: number) => `${apiUrl}api/v1/tours/${id}/report`,
    getSummaryReport: `${apiUrl}api/v1/tours/summarize-report`,
  },
  tourLog: {
    getAll: apiUrl + "api/v1/tourLogs",
    getById: apiUrl + "api/v1/tourLogs",
    deleteById: apiUrl + "api/v1/tourLogs",
    updateById: apiUrl + "api/v1/tourLogs",
    create: apiUrl + "api/v1/tourLogs",
  }
}

@Injectable({
  providedIn: 'root'
})

export class HttpProviderService {
  constructor(private webApiService: WebApiService) { }

  public getAllTours(): Observable<any> {
    return this.webApiService.get(httpLink.tour.getAll);
  }

  public getTourbyId(id: number): Observable<any> {
    return this.webApiService.get(`${httpLink.tour.getById}/${id}`);
  }

  public deleteTourById(id: number): Observable<any> {
    return this.webApiService.delete(`${httpLink.tour.deleteById}/${id}`);
  }

  public updateTourbyId(id: number, model: any): Observable<any> {
    return this.webApiService.put(`${httpLink.tour.updateById}/${id}`, model);
  }

  public createTour(model: any): Observable<any> {
    return this.webApiService.post(httpLink.tour.create, model);
  }

  public getReportUrl(id: number): Observable<any> {
    return this.webApiService.get(httpLink.tour.getReport(id));
  }

  public getSummaryReportUrl(): Observable<any> {
    return this.webApiService.get(httpLink.tour.getSummaryReport);
  }

  public getAllTourLogs(): Observable<any> {
    return this.webApiService.get(httpLink.tourLog.getAll);
  }

  public getTourLogById(id: number): Observable<any> {
    return this.webApiService.get(`${httpLink.tourLog.getById}/${id}`);
  }

  public deleteTourLogById(id: number): Observable<any> {
    return this.webApiService.delete(`${httpLink.tourLog.deleteById}/${id}`);
  }

  public updateTourLogById(id: number, model: any): Observable<any> {
    return this.webApiService.put(`${httpLink.tourLog.updateById}/${id}`, model);
  }

  public createTourLog(model: any): Observable<any> {
    return this.webApiService.post(httpLink.tourLog.create, model);
  }

  public downloadFile(url: string): Observable<Blob> {
    return this.webApiService.get(url);
  }
}
