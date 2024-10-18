import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AccountCreationUploadComponent } from './account-creation-upload.component';

describe('AccountCreationUploadComponent', () => {
  let component: AccountCreationUploadComponent;
  let fixture: ComponentFixture<AccountCreationUploadComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AccountCreationUploadComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(AccountCreationUploadComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
