import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddExternalOfferComponent } from './add-external-offer.component';

describe('AddExternalOfferComponent', () => {
  let component: AddExternalOfferComponent;
  let fixture: ComponentFixture<AddExternalOfferComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [AddExternalOfferComponent]
    });
    fixture = TestBed.createComponent(AddExternalOfferComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
