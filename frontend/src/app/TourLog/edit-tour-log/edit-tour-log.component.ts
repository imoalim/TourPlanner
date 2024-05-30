import { Component, OnInit } from '@angular/core';
import { HttpProviderService } from '../../Service/http-provider.service';
import { ActivatedRoute, Router } from '@angular/router';
import { NgForm } from '@angular/forms';

@Component({
  selector: 'app-edit-tour-log',
  templateUrl: './edit-tour-log.component.html',
  styleUrls: ['./edit-tour-log.component.scss']
})
export class EditTourLogComponent implements OnInit {
  logId!: number;
  tourLog: any = {};

  constructor(private httpProvider: HttpProviderService, private route: ActivatedRoute, private router: Router) { }

  ngOnInit(): void {
    this.logId = +this.route.snapshot.paramMap.get('logId')!;
    this.loadTourLog();
  }

  loadTourLog() {
    this.httpProvider.getTourLogById(this.logId).subscribe((data: any) => {
      this.tourLog = data.body;
    });
  }

  updateTourLog(form: NgForm) {
    if (form.valid) {
      this.httpProvider.updateTourLogById(this.logId, this.tourLog).subscribe(() => {
        this.router.navigate(['/tour-details', this.tourLog.tourId]);
      });
    }
  }
}
