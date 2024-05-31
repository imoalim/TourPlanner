import { Component, OnInit, ViewChild } from '@angular/core';
import { NgForm } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { HttpProviderService } from '../../Service/http-provider.service';

@Component({
  selector: 'app-add-tour-log',
  templateUrl: './add-tour-log.component.html',
  styleUrls: ['./add-tour-log.component.scss']
})
export class AddTourLogComponent implements OnInit {
  tourId!: number;
  addTourLogForm: TourLogForm = new TourLogForm();

  @ViewChild("tourLogForm")
  tourLogForm!: NgForm;
  isSubmitted: boolean = false;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private toastr: ToastrService,
    private httpProvider: HttpProviderService
  ) {}

  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      this.tourId = +params.get('id')!;
      if (isNaN(this.tourId)) {
        this.toastr.error('Invalid tour ID.');
        this.router.navigate(['/Home']);
      }
    });
  }

  addTourLog(isValid: boolean) {
    this.isSubmitted = true; // Mark the form as submitted
    if (isValid) {
      this.addTourLogForm.tourId = this.tourId;
      console.log('Sending data:', this.addTourLogForm); // Log the data being sent
      this.httpProvider.createTourLog(this.addTourLogForm).subscribe(
        (data: any) => {
          if (data) {
            this.toastr.success('Tour log successfully added.');
            setTimeout(() => {
              this.router.navigate(['/TourDetails', this.tourId]);
            }, 500);
          }
        },
        (error: any) => {
          this.toastr.error('Failed to add tour log.');
          console.error('Error adding tour log:', error);
        }
      );
    }
  }
}

export class TourLogForm {
  dateTime: string = "";
  comment: string = "";
  difficulty: string = "";
  distance: number = 0;
  totalTime: number = 0;
  rating: number = 0;
  tourId: number = 0;
}
