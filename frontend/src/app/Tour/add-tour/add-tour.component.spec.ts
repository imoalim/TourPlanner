import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddTourComponent } from './add-tour.component';

describe('AddTourComponent', () => {
  let component: AddTourComponent;
  let fixture: ComponentFixture<AddTourComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [AddTourComponent]
    });
    fixture = TestBed.createComponent(AddTourComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
