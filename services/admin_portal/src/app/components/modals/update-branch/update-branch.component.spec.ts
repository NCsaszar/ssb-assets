import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { HttpClientModule } from '@angular/common/http';
import { MatDialogModule, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { UpdateBranchComponent } from './update-branch.component';

describe('UpdateBranchComponent', () => {
  let component: UpdateBranchComponent;
  let fixture: ComponentFixture<UpdateBranchComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        MatDialogModule,
        HttpClientModule,
      ],
      providers: [
        { provide: MAT_DIALOG_DATA, useValue: {} },
        { provide: ActivatedRoute, useValue: {} } // Provide a mock value for MAT_DIALOG_DATA
      ]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(UpdateBranchComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});