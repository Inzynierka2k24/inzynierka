import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EditExternalOfferComponent } from './edit-external-offer.component';

describe('EditExternalOfferComponent', () => {
  let component: EditExternalOfferComponent;
  let fixture: ComponentFixture<EditExternalOfferComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [EditExternalOfferComponent]
    });
    fixture = TestBed.createComponent(EditExternalOfferComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
