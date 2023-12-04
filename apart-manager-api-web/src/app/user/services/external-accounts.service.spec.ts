import { TestBed } from '@angular/core/testing';

import { ExternalAccountsService } from './external-accounts.service';

describe('ExternalAccountsService', () => {
  let service: ExternalAccountsService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ExternalAccountsService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
