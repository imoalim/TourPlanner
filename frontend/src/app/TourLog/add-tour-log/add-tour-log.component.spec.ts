import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddTourLogComponent } from './add-tour-log.component';

describe('AddTourLogComponent', () => {
  let component: AddTourLogComponent;
  let fixture: ComponentFixture<AddTourLogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AddTourLogComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(AddTourLogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
