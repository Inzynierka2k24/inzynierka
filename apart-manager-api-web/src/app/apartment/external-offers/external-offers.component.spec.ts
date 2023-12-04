import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ExternalOffersComponent } from './external-offers.component';

describe('ExternalOffersComponent', () => {
  let component: ExternalOffersComponent;
  let fixture: ComponentFixture<ExternalOffersComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ExternalOffersComponent]
    });
    fixture = TestBed.createComponent(ExternalOffersComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
