import { Component, OnInit, Input } from '@angular/core';
import { HttpProviderService } from '../../Service/http-provider.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-tour-log-list',
  templateUrl: './tour-log-list.component.html',
  styleUrls: ['./tour-log-list.component.scss']
})
export class TourLogListComponent implements OnInit {
  @Input() tourId!: number;
  tourLogs: any[] = [];

  constructor(private httpProvider: HttpProviderService, private router: Router) { }

  ngOnInit(): void {
    this.loadTourLogs();
  }

  loadTourLogs() {
    this.httpProvider.getAllTourLogs().subscribe((data: any) => {
      this.tourLogs = data.body.filter((log: any) => log.tourId === this.tourId);
    });
  }

  deleteTourLog(logId: number) {
    this.httpProvider.deleteTourLogById(logId).subscribe(() => {
      this.loadTourLogs(); // Refresh the list of tour logs
    });
  }

  createLog() {
    this.router.navigate(['/AddTourLog', this.tourId]);
  }

  editLog(logId: number) {
    this.router.navigate(['/EditTourLog', logId]);
  }
}
