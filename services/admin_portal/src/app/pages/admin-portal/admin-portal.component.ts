import { Component, HostListener, ChangeDetectorRef } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { CookieService } from 'ngx-cookie-service';
import { CommonModule } from '@angular/common';
import { MatIconModule } from '@angular/material/icon';
import { FormsModule } from '@angular/forms';
import { Subject, debounceTime, distinctUntilChanged } from 'rxjs';
import { MatTableModule } from '@angular/material/table';
import { MatDividerModule } from '@angular/material/divider';
import { UserDetailsModalComponent } from '../../components/modals/user-details-modal/user-details-modal.component';
import { User } from '../../models/user.model';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatCardModule } from '@angular/material/card';
import { API_CONFIG } from '../../utils/config/apiConfig';

@Component({
  selector: 'app-admin-portal',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    MatTableModule,
    UserDetailsModalComponent,
    MatProgressSpinnerModule,
    MatCardModule,
    MatDividerModule,
    MatIconModule,
  ],
  templateUrl: './admin-portal.component.html',
  styleUrls: ['./admin-portal.component.css'],
})
export class AdminPortalComponent {
  users: any[] = []; // Stores the list of users fetched from the server.
  page: number = 0; // Current page of user data being viewed.
  pageSize: number = 10;
  loading: boolean = false;
  moreRecordsAvailable: boolean = true;
  searchCriterion: string = 'username'; // Default search criterion
  searchTerm: string = ''; // The current search term entered by the user.
  searchTermSubject = new Subject<string>(); // A subject to debounce and emit search term changes.
  displayedColumns: string[] = [
    'username',
    'email',
    'firstName',
    'lastName',
    'dateCreated',
    'isActive',
  ];
  selectedUser: any = null; // The user currently selected for detail view.
  showUserModal: boolean = false;
  modalKey: number = 0; // A key to force rerendering of the modal for new data.
  sortCriteria: 'dateCreated' | 'isActive' | 'username' | 'email' | 'firstName' | 'lastName' = 'dateCreated';
  sortDirection: 'asc' | 'desc' = 'asc';

  constructor(
    private http: HttpClient,
    private cookieService: CookieService,
    private cdr: ChangeDetectorRef
  ) {
    this.initializeSearchSubscriptions(); // Set up the search subscription.
    this.fetchFirstPage(); // Fetch the first page of users on component initialization.
  }

  // Sets up the RxJS pipeline for handling search term changes.
  initializeSearchSubscriptions(): void {
    this.searchTermSubject
      .pipe(
        debounceTime(500), // Wait for 500ms pauses between keystrokes.
        distinctUntilChanged() // Only emit when the current value is different from the last.
      )
      .subscribe(() => {
        this.resetAndSearchUsers(); // Perform the search action.
      });
  }

  toggleSortCriteria(criteria: 'dateCreated' | 'isActive' | 'username' | 'email' | 'firstName' | 'lastName'): void {
    if (this.sortCriteria === criteria) {
      this.sortDirection = this.sortDirection === 'asc' ? 'desc' : 'asc';
    } else {
      this.sortCriteria = criteria;
      this.sortDirection = 'asc'; // Default to ascending when criteria changes
    }
    this.sortUsers();
  }


  sortUsers(): void {
    this.users.sort((a, b) => {
      let comparison = 0;
      switch (this.sortCriteria) {
        case 'username':
        case 'email':
        case 'firstName':
        case 'lastName':
          comparison = a[this.sortCriteria].toLowerCase().localeCompare(b[this.sortCriteria].toLowerCase());
          break;
        case 'isActive':
          comparison = (a.isActive === b.isActive ? 0 : a.isActive ? -1 : 1);
          break;
        case 'dateCreated':
          const dateA = new Date(a.dateCreated).getTime();
          const dateB = new Date(b.dateCreated).getTime();
          comparison = dateA - dateB;
          break;
      }
      return this.sortDirection === 'asc' ? comparison : -comparison;
    });

    // Ensure to trigger change detection
    this.users = [...this.users];
  }



  openUserModal(user: any): void {
    this.selectedUser = user;
    this.showUserModal = true;
    this.modalKey = Math.random(); // Update the modal key to ensure it re-renders.
    this.cdr.detectChanges(); // Explicitly trigger change detection
  }

  closeUserModal(): void {
    this.showUserModal = false;
    this.modalKey = Math.random(); // force a change
    this.cdr.detectChanges(); // Explicitly trigger change detection
  }

  fetchFirstPage(): void {
    this.page = 0;
    this.users = []; // Ensure users array is reset for initial load
    this.fetchUsers();
  }

  resetAndSearchUsers(): void {
    this.page = 0; // Reset page number for search
    this.users = []; // Clear existing users for new search results
    this.moreRecordsAvailable = true; // Assume more records are available for search
    this.fetchUsers();
  }

  fetchUsers(loadMore = false): void {
    if (!this.moreRecordsAvailable || this.loading) {
      return;
    }

    this.loading = true;

    // let url = `localhost:8765/api/v1/users/search?page=${this.page}&size=${this.pageSize}`;
    let url = `${API_CONFIG.getUserDetailsEndpoint}/search?page=${this.page}&size=${this.pageSize}`;

    url += `&${this.searchCriterion}=${encodeURIComponent(this.searchTerm)}`;

    const token = this.cookieService.get('token');
    const headers = new HttpHeaders({ Authorization: `Bearer ${token}` });
    this.http.get<any>(url, { headers }).subscribe({
      next: (response) => {
        const newUsers = response.content || response;
        if (Array.isArray(newUsers) && newUsers.length) {
          this.users = [...this.users, ...newUsers];
          this.page++; // Increment page number only after successful fetch
          this.moreRecordsAvailable = newUsers.length === this.pageSize;
          this.sortUsers();
        } else {
          this.moreRecordsAvailable = false; // No more records to load
        }
        this.loading = false;
      },
      error: (error) => {
        console.error(error);
        this.loading = false;
        this.moreRecordsAvailable = false;
      },
    });
  }

  handleUserUpdate(event: User | 'deleted' | null): void {
    // Immediately close the modal and reset modal state
    this.showUserModal = false;
    this.modalKey++; // Increment to ensure modal re-renders with fresh data next time
    this.cdr.detectChanges(); // Ensure UI updates to reflect modal state change

    if (event === 'deleted') {
      this.resetAndSearchUsers(); // Refetch users to reflect deletions or to reset the list
      return;
    } else if (event) {
      // Handle the user update case
      const index = this.users.findIndex(
        (user) => user.userId === event.userId
      );
      if (index !== -1) {
        // If the user exists, update the specific entry with new data
        this.users[index] = { ...this.users[index], ...event };
      } else {
        // If the user doesn't exist in the list, add them
        this.users.push(event);
      }
      // Since user data was updated, refetch the user list to ensure UI consistency
      console.log('User updated, refetching list to ensure consistency.');
      this.resetAndSearchUsers(); // Resets and fetches the list to reflect the update
    } else {
      // Handle the modal close without changes case
      this.fetchUsers(); // Refetch users to potentially reflect any external changes
    }

    // Update the users array to trigger change detection and update the UI
    this.users = [...this.users];
    this.cdr.detectChanges(); // Force change detection to ensure UI is up to date
  }

  // Detects scroll events to fetch more users when the bottom of the page is reached.
  @HostListener('window:scroll', ['$event'])
  onWindowScroll(): void {
    const scrollTop =
      document.documentElement.scrollTop || document.body.scrollTop || 0;
    const windowHeight = window.innerHeight;
    const documentHeight = document.documentElement.scrollHeight;

    if (scrollTop + windowHeight >= documentHeight) {
      const isAtBottom = documentHeight - (scrollTop + windowHeight) <= 5;
      if (isAtBottom && !this.loading && this.moreRecordsAvailable) {
        this.fetchUsers(true);
      }
    }
  }
}
