import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UserCardSearchComponent } from './user-card-search.component';

describe('UserCardSearchComponent', () => {
  let component: UserCardSearchComponent;
  let fixture: ComponentFixture<UserCardSearchComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [UserCardSearchComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(UserCardSearchComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
