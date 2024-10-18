import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute, convertToParamMap } from '@angular/router';
import { HttpClientModule } from '@angular/common/http';
import { BankerDetailsComponent } from './banker-details.component';
import { of } from 'rxjs';

describe('BankerDetailsComponent', () => {
  let component: BankerDetailsComponent;
  let fixture: ComponentFixture<BankerDetailsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        BankerDetailsComponent,
        HttpClientModule,
      ],
      providers: [
        {
          provide: ActivatedRoute,
          // useValue: {
          //   snapshot: {
          //     paramMap: new Map().set('id', '1') // Mock paramMap with ID parameter
          //   }
          // }
          useValue: {
            params: of(convertToParamMap({ id: '1' }))
          }
        }
      ]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(BankerDetailsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});