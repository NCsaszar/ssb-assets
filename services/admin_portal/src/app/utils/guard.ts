import { Injectable } from '@angular/core';
import { AuthService } from './auth';

@Injectable({
  providedIn: 'root' // indicating that this guard service is intended to be a singleton and available throughout the application.
})
export class AuthGuard {
  constructor(
    private authService: AuthService) { }

  canActivate(): boolean {
    if (!this.authService.isLoggedIn()) {
      window.location.href = 'http://localhost:5173/';
      return false;
    }
    return true;
  }
}
