import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EditTourLogComponent } from './edit-tour-log.component';

describe('EditTourLogComponent', () => {
  let component: EditTourLogComponent;
  let fixture: ComponentFixture<EditTourLogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EditTourLogComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(EditTourLogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
