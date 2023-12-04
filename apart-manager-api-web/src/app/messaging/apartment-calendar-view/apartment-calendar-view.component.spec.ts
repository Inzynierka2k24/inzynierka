import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ApartmentCalendarViewComponent } from './apartment-calendar-view.component';

describe('ApartmentCalendarViewComponent', () => {
  let component: ApartmentCalendarViewComponent;
  let fixture: ComponentFixture<ApartmentCalendarViewComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ApartmentCalendarViewComponent]
    });
    fixture = TestBed.createComponent(ApartmentCalendarViewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
