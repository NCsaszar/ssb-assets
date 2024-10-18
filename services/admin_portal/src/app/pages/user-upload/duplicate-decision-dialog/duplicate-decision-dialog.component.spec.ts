import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DuplicateDecisionDialogComponent } from './duplicate-decision-dialog.component';

describe('DuplicateDecisionDialogComponent', () => {
  let component: DuplicateDecisionDialogComponent;
  let fixture: ComponentFixture<DuplicateDecisionDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DuplicateDecisionDialogComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(DuplicateDecisionDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
