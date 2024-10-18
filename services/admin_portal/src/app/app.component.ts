import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterOutlet } from '@angular/router';
import { NavigationComponent } from './components/navigation/navigation.component';
import { FooterComponent } from './components/footer/footer.component';
import { HomeComponent } from './pages/home/home.component';
import { SidebarComponent } from './components/sidebar/sidebar.component';
import { AuthService } from './utils/auth';
import { TokenValidationService } from './services/token-validation.service';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [
    CommonModule,
    RouterOutlet,
    NavigationComponent,
    FooterComponent,
    HomeComponent,
    SidebarComponent,
  ],
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
})
export class AppComponent implements OnInit {
  constructor(private authService: AuthService, private tokenValidationService: TokenValidationService) {}

  isLoggedIn(): boolean {
    return this.authService.isLoggedIn();
  }

  ngOnInit(): void {
    // The tokenValidationService will automatically start validating the token upon component initialization
  }

  title = 'secure-sentinel-bank-angular-frontend';
}
