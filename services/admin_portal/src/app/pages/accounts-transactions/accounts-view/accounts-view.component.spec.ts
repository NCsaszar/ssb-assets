import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AccountsViewComponent } from './accounts-view.component';

describe('AccountsViewComponent', () => {
  let component: AccountsViewComponent;
  let fixture: ComponentFixture<AccountsViewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AccountsViewComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(AccountsViewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
