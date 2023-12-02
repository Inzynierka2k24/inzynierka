import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ExternalAccountModalComponent } from './external-account-modal.component';

describe('AddExternalAccountModalComponent', () => {
  let component: ExternalAccountModalComponent;
  let fixture: ComponentFixture<ExternalAccountModalComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ExternalAccountModalComponent],
    });
    fixture = TestBed.createComponent(ExternalAccountModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
