import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ConfirmReversalModalComponent } from './confirm-reversal-modal.component';

describe('ConfirmReversalModalComponent', () => {
  let component: ConfirmReversalModalComponent;
  let fixture: ComponentFixture<ConfirmReversalModalComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ConfirmReversalModalComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ConfirmReversalModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
