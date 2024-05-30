import { Component, OnInit } from '@angular/core';
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
  tourId!: number;
  tour: any;
  tourLogs: any[] = [];

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private httpProvider: HttpProviderService,
    private toastr: ToastrService
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
}
