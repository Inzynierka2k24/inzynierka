import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UserWidgetComponent } from './user-widget.component';

describe('UserWidgetComponent', () => {
  let component: UserWidgetComponent;
  let fixture: ComponentFixture<UserWidgetComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [UserWidgetComponent]
    });
    fixture = TestBed.createComponent(UserWidgetComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
