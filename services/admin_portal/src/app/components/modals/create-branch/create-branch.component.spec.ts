import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { HttpClientModule } from '@angular/common/http';
import { CreateBranchComponent } from './create-branch.component';

describe('CreateBranchComponent', () => {
  let component: CreateBranchComponent;
  let fixture: ComponentFixture<CreateBranchComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        CreateBranchComponent,
        HttpClientModule,
      ],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: {
              paramMap: new Map().set('id', '1')
            },
          }
        }
      ]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(CreateBranchComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
