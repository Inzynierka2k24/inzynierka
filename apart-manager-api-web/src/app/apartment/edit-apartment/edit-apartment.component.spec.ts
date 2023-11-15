import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EditApartmentComponent } from './edit-apartment.component';

describe('EditApartmentComponent', () => {
  let component: EditApartmentComponent;
  let fixture: ComponentFixture<EditApartmentComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [EditApartmentComponent]
    });
    fixture = TestBed.createComponent(EditApartmentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
