import { Component } from '@angular/core';
import { HttpClient, HttpClientModule, HttpHeaders } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MatTableModule,  } from '@angular/material/table';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatSelectModule } from '@angular/material/select';
import { MatCardModule } from '@angular/material/card';
import { MatDividerModule } from '@angular/material/divider';
import { MatPaginator } from '@angular/material/paginator';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { CookieService } from 'ngx-cookie-service';


@Component({
  standalone: true,
  imports:[CommonModule, MatTableModule, FormsModule, MatIconModule, MatButtonModule, MatSelectModule,MatCardModule, MatDividerModule, MatPaginator,MatCardModule, MatFormFieldModule,MatInputModule],
  selector: 'app-user-search',
  templateUrl: './user-search.component.html',
  styleUrls: ['./user-search.component.css']
})
export class UserSearchComponent {
  searchCriterion: string = 'firstName';
  searchTerm: string = '';
  searchResults: any[] = [];
  selectedUser: any;
  userLoans: any[] = [];

  constructor(private http: HttpClient, private cookieService: CookieService) { }

  search() {
    this.getUserLoans();
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

  approveLoan(userLoanID: number) {
    const headers = new HttpHeaders().set('Authorization', 'Bearer ' + this.cookieService.get('token'));

    this.http.put<any>('/api/v1/loans/approve', {}, {
      headers: headers,
      params:{userLoanID:userLoanID}
    }).subscribe(response => {
      console.log('Loan approved successfully:', response);
      this.getUserLoans();
    }, error => {
      console.error('Error approving loan:', error);
      this.getUserLoans();
    });
  }

  getUserLoans() {
    console.log('Selected user:', this.selectedUser);
    console.log('Selected user ID:', this.selectedUser?.userId);
    if (this.selectedUser && this.selectedUser.userId) {
      const headers = new HttpHeaders().set('Authorization', 'Bearer ' + this.cookieService.get('token'));

      console.log('UserID:', this.selectedUser.userID);

      const userId = this.selectedUser.userId;

      this.http.get<any[]>('/api/v1/loans/user-loan?userID=' + userId, {
        headers: headers
      }).subscribe(loans => {
        this.userLoans = loans;
      });
    } else {
      this.userLoans = [];
    }
  }
}
