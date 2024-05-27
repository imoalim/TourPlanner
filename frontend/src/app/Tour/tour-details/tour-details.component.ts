import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpProviderService } from '../../Service/http-provider.service';
import * as L from 'leaflet';

@Component({
  selector: 'app-tour-details',
  templateUrl: './tour-details.component.html',
  styleUrls: ['./tour-details.component.scss']
})
export class TourDetailsComponent implements OnInit {
  tourId: number | null = null;
  distance: number | null = null;
  estimatedTime: number | null = null;

  constructor(
    private route: ActivatedRoute,
    private httpProvider: HttpProviderService
  ) {}

  ngOnInit(): void {
    const tourIdParam = this.route.snapshot.paramMap.get('tourId');
    if (tourIdParam) {
      this.tourId = +tourIdParam;
      this.loadMapData(this.tourId);
    } else {
      console.error('Tour ID not found in route parameters');
    }
  }

  async loadMapData(tourId: number) {
    try {
      const response = await this.httpProvider.getTourbyId(tourId).toPromise();
      if (response && response.body) {
        const mapData = response.body;
        this.distance = mapData.distance;
        this.estimatedTime = mapData.estimatedTime;
        const mapInfoDTO = mapData.mapInfoDTO;

        if (mapInfoDTO) {
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
          } else {
            console.error("No route data available to display on the map");
          }

          let startIcon = L.divIcon({ className: 'start-icon' });
          let endIcon = L.divIcon({ className: 'end-icon' });

          L.marker(startPoint, { icon: startIcon }).addTo(map).bindPopup("Start Point");
          L.marker(endPoint, { icon: endIcon }).addTo(map).bindPopup("End Point");
        } else {
          console.error("mapInfoDTO not found in response");
        }
      } else {
        console.error("No response from the server");
      }
    } catch (error) {
      console.error("Error loading map data:", error);
    }
  }
}
