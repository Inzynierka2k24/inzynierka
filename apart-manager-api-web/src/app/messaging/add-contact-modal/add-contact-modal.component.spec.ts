import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddContactModalComponent } from './add-contact-modal.component';

describe('AddContactModalComponent', () => {
  let component: AddContactModalComponent;
  let fixture: ComponentFixture<AddContactModalComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [AddContactModalComponent]
    });
    fixture = TestBed.createComponent(AddContactModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
