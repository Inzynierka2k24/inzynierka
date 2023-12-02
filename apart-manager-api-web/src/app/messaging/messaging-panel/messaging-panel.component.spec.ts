import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MessagingPanelComponent } from './messaging-panel.component';

describe('MessagingPanelComponent', () => {
  let component: MessagingPanelComponent;
  let fixture: ComponentFixture<MessagingPanelComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [MessagingPanelComponent]
    });
    fixture = TestBed.createComponent(MessagingPanelComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
