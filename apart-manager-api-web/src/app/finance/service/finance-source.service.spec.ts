import { TestBed } from '@angular/core/testing';

import { FinanceSourceService } from './finance-source.service';

describe('FinanceSourceService', () => {
  let service: FinanceSourceService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(FinanceSourceService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
