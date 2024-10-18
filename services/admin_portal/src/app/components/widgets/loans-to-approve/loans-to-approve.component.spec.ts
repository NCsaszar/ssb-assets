import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LoansToApproveComponent } from './loans-to-approve.component';

describe('LoansToApproveComponent', () => {
  let component: LoansToApproveComponent;
  let fixture: ComponentFixture<LoansToApproveComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [LoansToApproveComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(LoansToApproveComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
