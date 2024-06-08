import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TourLogListComponent } from './tour-log-list.component';

describe('TourLogListComponent', () => {
  let component: TourLogListComponent;
  let fixture: ComponentFixture<TourLogListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TourLogListComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(TourLogListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
