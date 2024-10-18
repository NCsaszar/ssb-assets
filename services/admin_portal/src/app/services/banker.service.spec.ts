import { TestBed } from '@angular/core/testing';
import { HttpClientModule } from '@angular/common/http';
import { BankerService } from './banker.service';

describe('BankerService', () => {
  let service: BankerService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientModule],
    });
    service = TestBed.inject(BankerService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
