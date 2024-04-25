import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Tour } from './models/tour.model';
import { TourService } from './core/services/tour.service';
import { HttpErrorResponse } from '@angular/common/http';
import { NgForm } from '@angular/forms';
import * as bootstrap from 'bootstrap';


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
        this.getTours()
    }

  public getTours(): void{
    this.tourService.getAllTours().subscribe(
      (response: Tour[]) => {
        this.tours = response;
      },
      (err: HttpErrorResponse) => {
        alert(err.message);
      }
    )
  }

  public addTour(tourData: any) {
    this.tourService.addTour(tourData).subscribe(
      (response) => {
        // Behandeln Sie die erfolgreiche Antwort - z.B. das Modal schließen und die Liste aktualisieren
        this.getTours();
        console.log(this.getTours())
      },
      (error: HttpErrorResponse) => {
        // Behandeln Sie Fehler - z.B. Fehlermeldung anzeigen
        console.error(error);
      }
    );
  }

  onSubmit(form: NgForm) {
    if (form.valid) {
      const newTour: Tour = form.value;
      this.tourService.addTour(newTour).subscribe({
        next: (tour) => {
          this.tours.push(tour);
          this.hideModal();
        },
        error: (error) => {
          console.error(error);
        }
      });
    }
  }

  hideModal() {
    const addTourModal = new bootstrap.Modal(
      document.getElementById('addTourModal')
    );
    addTourModal.hide();
  }
}
