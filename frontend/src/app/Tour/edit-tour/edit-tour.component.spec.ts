import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EditTourComponent } from './edit-tour.component';

describe('EditTourComponent', () => {
  let component: EditTourComponent;
  let fixture: ComponentFixture<EditTourComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [EditTourComponent]
    });
    fixture = TestBed.createComponent(EditTourComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
