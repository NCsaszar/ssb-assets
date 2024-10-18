import { ComponentFixture, TestBed } from '@angular/core/testing';

import { OpenAccountFormComponent } from './open-account-form.component';

describe('OpenAccountFormComponent', () => {
  let component: OpenAccountFormComponent;
  let fixture: ComponentFixture<OpenAccountFormComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [OpenAccountFormComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(OpenAccountFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
