import { TourLog } from "./tour-log.model";

export interface Tour {
  id: number;
  name: string;
  description: string;
  fromLocation: string;
  toLocation: string;
  transportType: string;
  distance: number;
  estimatedTime: number;
  imageUrl: string;
  // Wir werden das `TourLog`-Modell als Interface definieren und hier referenzieren.
  logs: TourLog[];
}
