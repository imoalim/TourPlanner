import { Component, Input, Output, EventEmitter } from '@angular/core';

@Component({
  selector: 'app-star-rating',
  templateUrl: './star-rating.component.html',
  styleUrls: ['./star-rating.component.scss']
})
export class StarRatingComponent {
  @Input() rating: number = 0;
  @Output() ratingChange: EventEmitter<number> = new EventEmitter<number>();
  stars: boolean[] = Array(5).fill(false);

  constructor() {}

  ngOnInit(): void {
    this.calculateStars();
  }

  calculateStars() {
    this.stars = this.stars.map((_, index) => index < this.rating);
  }

  onStarClick(index: number) {
    this.rating = index + 1;
    this.ratingChange.emit(this.rating);
    this.calculateStars();
  }
}
