import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute, convertToParamMap } from '@angular/router';
import { HttpClientModule } from '@angular/common/http';
import { AppointmentDetailsComponent } from './appointment-details.component';
import { of } from 'rxjs';

describe('AppointmentDetailsComponent', () => {
  let component: AppointmentDetailsComponent;
  let fixture: ComponentFixture<AppointmentDetailsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        AppointmentDetailsComponent,
        HttpClientModule,
      ],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            // snapshot: {
            //   paramMap: new Map().set('id', '1')
            // }
            params: of(convertToParamMap({ id: '1' }))
          }
        }
      ]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(AppointmentDetailsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});