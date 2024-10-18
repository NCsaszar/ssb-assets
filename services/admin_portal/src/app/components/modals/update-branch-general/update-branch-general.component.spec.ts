import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute, convertToParamMap } from '@angular/router';
import { HttpClientModule } from '@angular/common/http';
import { MatDialogModule, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { UpdateBranchGeneralComponent } from './update-branch-general.component';

describe('UpdateBranchGeneralComponent', () => {
  let component: UpdateBranchGeneralComponent;
  let fixture: ComponentFixture<UpdateBranchGeneralComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        MatDialogModule,
        HttpClientModule,
      ],
      providers: [
        { provide: MAT_DIALOG_DATA, useValue: {} }, // Provide a mock value for MAT_DIALOG_DATA
        { 
          provide: ActivatedRoute, 
          useValue: { 
            snapshot: { 
              paramMap: convertToParamMap({ id: '1' }) // Mock paramMap with ID parameter
            }
          } 
        }
      ]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(UpdateBranchGeneralComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});