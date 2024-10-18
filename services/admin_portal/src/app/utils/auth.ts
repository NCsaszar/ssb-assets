import { Injectable } from '@angular/core';
import { CookieService } from 'ngx-cookie-service';
import { Router } from '@angular/router';
import { decodeToken, isTokenValid, getUserRole } from './token-utils';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  constructor(private cookieService: CookieService, private router: Router) {}

  getUserPayload() {
    const token = this.cookieService.get('token');
    return token ? decodeToken(token) : null;
  }

  isLoggedIn(): boolean {
    return isTokenValid(this.cookieService);
  }

  isAdmin(): boolean {
    return getUserRole(this.cookieService) === 'ADMIN';
  }

  isCustomer(): boolean {
    return getUserRole(this.cookieService) === 'CUSTOMER';
  }

  signOut(): void {
    this.cookieService.delete('token');
    // window.location.href = `http://${hostname}:80/`;
    window.location.href = `http://localhost:5173/`;
  }
}
