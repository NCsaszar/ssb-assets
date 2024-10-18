import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { CookieService } from 'ngx-cookie-service';
import { BranchService } from './branch.service';
import { Branch } from '../models/branch.model';

describe('BranchService', () => {
  let service: BranchService;
  let httpTestingController: HttpTestingController;


  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [CookieService]
    });
    service = TestBed.inject(BranchService);
    httpTestingController = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpTestingController.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should get all branches', () => {
    const dummyBranches: Branch[] = [
      // Provide dummy branches here
    ];

    service.getAllBranches().subscribe(branches => {
      expect(branches.length).toBe(dummyBranches.length);
      expect(branches).toEqual(dummyBranches);
    });

    const req = httpTestingController.expectOne('http://localhost:8765/BRANCH-SERVICE/api/v1/branch');
    expect(req.request.method).toBe('GET');
    req.flush(dummyBranches);
  });


});
