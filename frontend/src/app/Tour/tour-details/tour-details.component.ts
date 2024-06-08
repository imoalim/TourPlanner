import { Component, OnInit, ElementRef, ViewChild } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpProviderService } from '../../Service/http-provider.service';
import * as L from 'leaflet';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-tour-details',
  templateUrl: './tour-details.component.html',
  styleUrls: ['./tour-details.component.scss']
})
export class TourDetailsComponent implements OnInit {
  tour: any;
  tourId!: number;
  reportUrl: string | undefined;
  summaryReportUrl: string | undefined;

  @ViewChild('downloadLink', { static: false }) downloadLink!: ElementRef;
  @ViewChild('summaryDownloadLink', { static: false }) summaryDownloadLink!: ElementRef;
  tourLogs: any;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private toastr: ToastrService,
    private httpProvider: HttpProviderService
  ) {}

  ngOnInit(): void {
    this.tourId = +this.route.snapshot.paramMap.get('tourId')!;
    this.loadTourDetails();
    this.loadTourLogs();
  }

  loadTourDetails() {
    this.httpProvider.getTourbyId(this.tourId).subscribe((response) => {
      this.tour = response.body;
      this.loadMapData();
    });
  }

  loadTourLogs() {
    this.httpProvider.getAllTourLogs().subscribe((response) => {
      this.tourLogs = response.body.filter((log: any) => log.tourId === this.tourId);
    });
  }

  loadMapData() {
    const mapInfoDTO = this.tour.mapInfoDTO;
    let startPoint: [number, number] = [mapInfoDTO.startLat, mapInfoDTO.startLng];
    let endPoint: [number, number] = [mapInfoDTO.endLat, mapInfoDTO.endLng];
    let centerPoint: [number, number] = [mapInfoDTO.centerLat, mapInfoDTO.centerLng];
    let route: [number, number][] = JSON.parse(mapInfoDTO.route);

    let map = L.map('mapid').setView(centerPoint, 14);

    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
      maxZoom: 19,
      attribution: '© OpenStreetMap'
    }).addTo(map);

    if (route.length > 0) {
      let polyline = L.polyline(route, { color: 'blue' }).addTo(map);
      map.fitBounds(polyline.getBounds());
    }

    let startIcon = L.divIcon({ className: 'start-icon' });
    let endIcon = L.divIcon({ className: 'end-icon' });

    L.marker(startPoint, { icon: startIcon }).addTo(map).bindPopup("Start Point");
    L.marker(endPoint, { icon: endIcon }).addTo(map).bindPopup("End Point");
  }

  editTour() {
    this.router.navigate(['/EditTour', this.tourId]);
  }
  createTourLog() {
    this.router.navigate(['/AddTourLog', this.tour.id]);
  }

  deleteTourLog(logId: number) {
    this.httpProvider.deleteTourLogById(logId).subscribe(() => {
      this.toastr.success('Tour log deleted successfully');
      this.loadTourLogs();
    });
  }

  
  fetchReportUrl() {
    this.httpProvider.getReportUrl(this.tourId).subscribe((data: any) => {
      this.reportUrl = data.body.reportURL;
      if (this.reportUrl) {
        this.triggerDownload(this.reportUrl);
      } else {
        this.toastr.error('Failed to fetch report URL.');
      }
    }, error => {
      this.toastr.error('Error fetching report URL.');
      console.error('Error fetching report URL:', error);
    });
  }

  fetchSummaryReportUrl() {
    this.httpProvider.getSummaryReportUrl().subscribe((data: any) => {
      this.summaryReportUrl = data.body.reportURL;
      console.log(this.summaryReportUrl)
      if (this.summaryReportUrl) {
        this.triggerDownload(this.summaryReportUrl);
      } else {
        this.toastr.error('Failed to fetch summary report URL.');
      }
    }, error => {
      this.toastr.error('Error fetching summary report URL.');
      console.error('Error fetching summary report URL:', error);
    });
  }

  triggerDownload(url: string) {
    window.open(url);
  }
}