import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UpdateBankerComponent } from './update-banker.component';

describe('UpdateBankerComponent', () => {
  let component: UpdateBankerComponent;
  let fixture: ComponentFixture<UpdateBankerComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [UpdateBankerComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(UpdateBankerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
