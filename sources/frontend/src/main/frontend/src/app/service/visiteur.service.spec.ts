import { TestBed } from '@angular/core/testing';

import { VisiteurService } from './visiteur.service';

describe('VisiteurService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: VisiteurService = TestBed.get(VisiteurService);
    expect(service).toBeTruthy();
  });
});
