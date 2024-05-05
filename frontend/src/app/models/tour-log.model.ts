export interface TourLog {
  id: number;
  dateTime: Date;
  comment: string;
  difficulty: string;
  distance: number;
  totalTime: number;
  rating: number;
  // tour: Tour; // Optional, wenn Sie eine Referenz zur Tour halten möchten
}
