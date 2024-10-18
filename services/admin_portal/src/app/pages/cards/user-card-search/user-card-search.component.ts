import { Component,ViewChild, ChangeDetectorRef } from '@angular/core';
import { HttpClient, HttpClientModule, HttpHeaders, HttpRequest, HttpResponse} from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MatTableModule, MatTableDataSource  } from '@angular/material/table';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatSelectModule } from '@angular/material/select';
import { MatCard, MatCardModule } from '@angular/material/card';
import { MatDividerModule } from '@angular/material/divider';
import { MatPaginator } from '@angular/material/paginator';
import { MatInputModule } from '@angular/material/input';
import { CookieService } from 'ngx-cookie-service';

@Component({
  selector: 'app-user-card-search',
  standalone: true,
  imports:[CommonModule, MatTableModule, FormsModule, MatIconModule, MatButtonModule, MatSelectModule,MatCardModule,MatDividerModule,MatPaginator,MatInputModule,MatSelectModule],
  templateUrl: './user-card-search.component.html',
  styleUrl: './user-card-search.component.css'
})
export class UserCardSearchComponent {
  searchCriterion: string = 'firstName';
  searchTerm: string = '';
  searchResults: any[] = [];
  selectedUser: any;
  userCards: MatTableDataSource<any> = new MatTableDataSource<any>();
  filterCriterion: string = 'cardID';
  filterValue: string = '';
  @ViewChild(MatPaginator) paginator!: MatPaginator;

  constructor(private cdr: ChangeDetectorRef, private http: HttpClient, private cookieService: CookieService) { }

  ngOnInit(): void {
    this.getUserCards();
  }

  search() {
    this.getUserCards();
    console.log(this.searchTerm);
    if (this.searchTerm.trim() !== '') {
      const headers = new HttpHeaders().set('Authorization', 'Bearer ' + this.cookieService.get('token'));

      const params: any = {};
      params[this.searchCriterion] = this.searchTerm;

      this.http.get<any>('/api/v1/users/search', {
        headers: headers,
        params: params
      }).subscribe(response => {
        console.log('Received users:', response);
        this.searchResults = response.content;
      });
    } else {
      this.searchResults = [];
    }
  }

  getUserCards() {
    console.log('Selected user:', this.selectedUser);
    console.log('Selected user ID:', this.selectedUser?.userId);
    if (this.selectedUser && this.selectedUser.userId) {
      const headers = new HttpHeaders().set('Authorization', 'Bearer ' + this.cookieService.get('token'));

      const userId = this.selectedUser.userId;

      this.http.get<any>('/api/v1/cards/user-card', {
        headers: headers,
        params: { userID: userId }
      }).subscribe(
        cards => {
          console.log('User cards:', cards);
          this.userCards = new MatTableDataSource<any>(cards);
          this.userCards.paginator = this.paginator;
        },
        error => {
          console.error('Error fetching user cards:', error);
          this.userCards = new MatTableDataSource<any>();
        }
      );
    } else {
      console.error('Error fetching user cards: Selected user or user ID not found');
      this.userCards = new MatTableDataSource<any>();
    }
  }

  activateCard(cardID: number) {
    const headers = new HttpHeaders().set('Authorization', 'Bearer ' + this.cookieService.get('token'));

    this.http.put('/api/v1/cards/activate', {}, {
      headers: headers,
      params: { cardID: cardID },
      observe: 'response',
      responseType: 'text'
    }).subscribe((response: HttpResponse<string>) => {
      console.log('Response from server:', response.body);
      if (response.body?.startsWith('Card activated successfully')) {

        const activatedCard = this.userCards.data.find(card => card.cardID === cardID);
        if (activatedCard) {
          activatedCard.cardStatus = true;
        }
        this.getUserCards();
      }
    }, error => {
      console.error('Error activating card:', error);
    });
  }

  disableCard(cardID: number) {
    const headers = new HttpHeaders().set('Authorization', 'Bearer ' + this.cookieService.get('token'));

    const body = { cardID: cardID };

this.http.delete('/api/v1/cards/user-card', {
  headers: headers,
  params: { cardID: cardID },
  observe: 'response',
  responseType: 'text'
}).subscribe((response: HttpResponse<string>) => {
  console.log('Response from server:', response.body);

  if (response.body?.includes('Card deleted successfully')) {
    const deactivatedCard = this.userCards.data.find(card => card.cardID === cardID);
    if (deactivatedCard) {
      deactivatedCard.cardStatus = false;
    }
  }
}, error => {
  console.error('Error deactivating card:', error);
});
  }

  applyFilter() {
    if (this.filterValue.trim() !== '') {
      const headers = new HttpHeaders().set('Authorization', 'Bearer ' + this.cookieService.get('token'));

      const params: any = {};
      params[this.filterCriterion] = this.filterValue;

      this.http.get<any>('/api/v1/cards/user-card', {
        headers: headers,
        params: params
      }).subscribe(response => {
        console.log('Filtered cards:', response);
        this.userCards = new MatTableDataSource<any>(response);
        this.userCards.paginator = this.paginator;
      });
    } else {

    }
};
}
