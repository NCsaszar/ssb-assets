import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpClientModule } from '@angular/common/http';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { BankerComponent } from './banker.component';

describe('BankerComponent', () => {
  let component: BankerComponent;
  let fixture: ComponentFixture<BankerComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        BankerComponent,
        HttpClientModule,
        BrowserAnimationsModule,
      ]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(BankerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});