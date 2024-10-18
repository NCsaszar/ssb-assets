import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute, convertToParamMap } from '@angular/router';
import { HttpClientModule } from '@angular/common/http';
import { BranchDetailsComponent } from './branch-details.component';
import { of } from 'rxjs';

describe('BranchDetailsComponent', () => {
  let component: BranchDetailsComponent;
  let fixture: ComponentFixture<BranchDetailsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        BranchDetailsComponent,
        HttpClientModule,
      ],
      providers: [
        { provide: ActivatedRoute,
          // useValue: {},
          useValue: {
            params: of(convertToParamMap({ id: '1' }))
          }
        }
      ]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(BranchDetailsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
