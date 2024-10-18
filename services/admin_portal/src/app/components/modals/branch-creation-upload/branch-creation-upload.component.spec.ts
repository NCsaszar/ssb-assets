import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BranchCreationUploadComponent } from './branch-creation-upload.component';

describe('BranchCreationUploadComponent', () => {
  let component: BranchCreationUploadComponent;
  let fixture: ComponentFixture<BranchCreationUploadComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [BranchCreationUploadComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(BranchCreationUploadComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
