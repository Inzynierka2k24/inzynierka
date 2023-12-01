import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddExternalAccountModalComponent } from './add-external-account-modal.component';

describe('AddExternalAccountModalComponent', () => {
  let component: AddExternalAccountModalComponent;
  let fixture: ComponentFixture<AddExternalAccountModalComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [AddExternalAccountModalComponent]
    });
    fixture = TestBed.createComponent(AddExternalAccountModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
