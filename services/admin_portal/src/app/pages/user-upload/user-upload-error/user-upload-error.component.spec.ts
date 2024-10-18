import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UserUploadErrorComponent } from './user-upload-error.component';

describe('UserUploadErrorComponent', () => {
  let component: UserUploadErrorComponent;
  let fixture: ComponentFixture<UserUploadErrorComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [UserUploadErrorComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(UserUploadErrorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
