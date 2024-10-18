import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { HttpClientModule } from '@angular/common/http';
import { CreateBankerComponent } from './create-banker.component';

describe('CreateBankerComponent', () => {
  let component: CreateBankerComponent;
  let fixture: ComponentFixture<CreateBankerComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        CreateBankerComponent,
        HttpClientModule,
      ],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: {
              paramMap: new Map().set('id', '1') // Mock paramMap with ID parameter
            }
          }
        }
      ]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(CreateBankerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});


  // beforeEach(async () => {
  //   await TestBed.configureTestingModule({
  //     imports: [CreateBankerComponent]
  //   })
  //   .compileComponents();
    
  //   fixture = TestBed.createComponent(CreateBankerComponent);
  //   component = fixture.componentInstance;
  //   fixture.detectChanges();
  // });

  // it('should create', () => {
  //   expect(component).toBeTruthy();
  // });
