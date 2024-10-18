import { TestBed } from '@angular/core/testing';
import { HttpClientModule } from '@angular/common/http';
import { AppointmentService } from './appointment.service';

describe('AppointmentService', () => {
  let service: AppointmentService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientModule],
    });
    service = TestBed.inject(AppointmentService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
