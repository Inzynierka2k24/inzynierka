import { TestBed } from '@angular/core/testing';

import { ExternalOfferService } from './external-offer.service';

describe('ExternalOfferService', () => {
  let service: ExternalOfferService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ExternalOfferService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
