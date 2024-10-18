import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { trigger, state, style, transition, animate } from '@angular/animations';
import { getUsername } from '../../utils/token-utils';
import { CookieService } from 'ngx-cookie-service';

@Component({
  selector: 'app-sidebar',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './sidebar.component.html',
  styleUrls: ['./sidebar.component.css'],
  animations: [
    trigger('appear', [
      state('in', style({ opacity: 1, transform: 'translateX(0)' })),
      transition(':enter', [
        style({ opacity: 0, transform: 'translateX(-100%)' }),
        animate('0.5s ease-out')
      ]),
    ]),
  ],
})
export class SidebarComponent {
  public expandedGroups: { [key: string]: boolean } = {};

  constructor(
    private router: Router,
    private cookieService: CookieService
  ) { }

  getUsername(): string | null {
    return getUsername(this.cookieService);
  }

  navigateTo(route: string) {
    this.router.navigate([route]);
  }

  toggleGroup(groupName: string): void {
    this.expandedGroups[groupName] = !this.expandedGroups[groupName];
  }
}
