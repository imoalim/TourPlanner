import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Tour } from './models/tour.model';
import { TourService } from './core/services/tour.service';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit{
  title:string = 'Tour Planner';
  public tours: Tour[] = [];

  //constructor(private router: Router) {} // Router injizieren
  constructor(private tourService: TourService) {
  }

  ngOnInit(): void {
        this.getTour()
    }

  public getTour(): void{
    this.tourService.getAllTours().subscribe(
      (response: Tour[]) => {
        this.tours = response;
      },
      (err: HttpErrorResponse) => {
        alert(err.message);
      }
    )
  }

  /*navigateToCreateTour() {
    this.router.navigate(['/create-tour']); // Navigieren zur CreateTourComponent
  }*/
}
