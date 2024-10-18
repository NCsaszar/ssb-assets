import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../utils/auth';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-navigation',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './navigation.component.html',
  styleUrls: ['./navigation.component.css'],
})
export class NavigationComponent {
  showModal = false;
  isMenuVisible = false;
  isSigningIn = true;

  constructor(public authService: AuthService, private router: Router) {}

  toggleMenu() {
    this.isMenuVisible = !this.isMenuVisible;
  }

  handleSignOut() {
    this.authService.signOut();
  }

  handleSignClick() {
    if (!this.authService.isLoggedIn) {
      this.isSigningIn = true;
      this.showModal = true;
    } else {
      this.handleSignOut();
    }
  }

  switchModal() {
    this.isSigningIn = !this.isSigningIn;
  }
}
