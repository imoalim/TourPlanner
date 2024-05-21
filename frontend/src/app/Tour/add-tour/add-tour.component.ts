import { Component, OnInit, ViewChild } from '@angular/core';
import { NgForm } from '@angular/forms';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { HttpProviderService } from 'src/app/Service/http-provider.service';

@Component({
  selector: 'app-add-tour',
  templateUrl: './add-tour.component.html',
  styleUrls: ['./add-tour.component.scss']
})

export class AddTourComponent implements OnInit {
  addTourForm: tourForm = new tourForm();

  @ViewChild("tourForm")
  tourForm!: NgForm;
  isSubmitted: boolean = false;
  constructor(private router: Router, private httpProvider: HttpProviderService, private toastr: ToastrService) { }

  ngOnInit(): void {  }

  AddTour(isValid: boolean) {
    this.isSubmitted = true; // Markiert das Formular als eingereicht
    if (isValid) {
      console.log('Sending data:', this.addTourForm); // Loggen der zu sendenden Daten
      this.httpProvider.createTour(this.addTourForm).subscribe(data => {
        if (data) {
          this.toastr.success('Tour successfully added.');
          setTimeout(() => {
            this.router.navigate(['/Home']);
          }, 500);
        }
      }, error => {
        this.toastr.error('Failed to add tour.');
        console.error('Error adding tour:', error);
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

