import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddOrderModalComponent } from './add-order-modal.component';

describe('AddOrderModalComponent', () => {
  let component: AddOrderModalComponent;
  let fixture: ComponentFixture<AddOrderModalComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [AddOrderModalComponent]
    });
    fixture = TestBed.createComponent(AddOrderModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
