import { Injectable, OnDestroy } from '@angular/core';
import { AuthService } from '../utils/auth';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class TokenValidationService implements OnDestroy {
  private intervalId: any;

  constructor(private authService: AuthService, private router: Router) {
    this.startTokenValidation();
  }

  startTokenValidation(): void {
    this.intervalId = setInterval(() => {
      if (!this.authService.isLoggedIn()) {
        this.authService.signOut();
      }
    }, 60000); // Check every 10 min
  }

  ngOnDestroy(): void {
    clearInterval(this.intervalId);
  }
}
