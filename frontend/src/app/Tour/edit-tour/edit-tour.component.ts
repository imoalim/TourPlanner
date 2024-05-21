import { Component, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { NgForm } from '@angular/forms';
import { ToastrService } from 'ngx-toastr';
import { HttpProviderService } from 'src/app/Service/http-provider.service';

@Component({
  selector: 'app-edit-tour',
  templateUrl: './edit-tour.component.html',
  styleUrls: ['./edit-tour.component.scss']
})
export class EditTourComponent implements OnInit {
  editTourForm: tourForm = new tourForm();
  @ViewChild('tourForm') tourForm!: NgForm;
  isSubmitted: boolean = false;
  tourId: number = 0;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private httpProvider: HttpProviderService,
    private toastr: ToastrService
  ) { }

  ngOnInit(): void {
    this.tourId = this.route.snapshot.params['TourId'];
    this.loadTourData();
  }

  loadTourData(): void {
    this.httpProvider.getTourbyId(this.tourId).subscribe(data => {
      if (data) {
        this.editTourForm = data;
      }
    }, error => {
      this.toastr.error('Failed to load tour data.');
      console.error('Error loading tour data:', error);
    });
  }

  updateTour(isValid: boolean): void {
    this.isSubmitted = true;
    if (isValid) {
      this.httpProvider.updateTourbyId(this.tourId, this.editTourForm).subscribe(data => {
        if (data) {
          this.toastr.success('Tour successfully updated.');
          setTimeout(() => {
            this.router.navigate(['/Home']);
          }, 500);
        }
      }, error => {
        this.toastr.error('Failed to update tour.');
        console.error('Error updating tour:', error);
      });
    }
  }
}

export class tourForm {
  name: string = "";
  description: string = "";
  fromLocation: string = "";
  toLocation: string = "";
  transportType: string = "";
  distance: number = 0;
  duration: number = 0;
  imageUrl: string = "";
}
