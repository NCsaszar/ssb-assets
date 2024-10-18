import { Component, OnInit } from '@angular/core';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { CommonModule } from '@angular/common';

interface Loan {
  userLoanID: number;
  loanID: number;
  userID: number;
  accountID: number;
  loanAmount: number;
  loanStatus: boolean;
  loanStartDate: string | null;
  loanEndDate: string | null;
}

@Component({
  selector: 'app-loans-to-approve',
  standalone: true,
  imports: [CommonModule,HttpClientModule],
  templateUrl: './loans-to-approve.component.html',
  styleUrl: './loans-to-approve.component.css'
})
export class LoansToApproveComponent implements OnInit {
  loans: Loan[] = [];
  authToken: string = 'eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiQURNSU4iLCJ1c2VySWQiOjY1Mywic3ViIjoidXNlcjEiLCJpYXQiOjE3MDU1OTM3NjQsImV4cCI6MTkwNTYyOTc2NH0.oiDGzkVNL_ja3Xog9CBl3FFZMo8SpM9EZnvTfZF9c0k'; 

  constructor(private http: HttpClient) { }

  ngOnInit(): void {
    this.fetchLoansToApprove();
  }

  fetchLoansToApprove() {
    this.http.get<Loan[]>('http://localhost:8765/CARDSLOANS-SERVICE/api/v1/loans/admin/approve', { headers: { Authorization: `Bearer ${this.authToken}` } })
      .subscribe(
        (response) => {
          this.loans = response;
        },
        (error) => {
          console.error('Error fetching loans:', error);
        }
      );
  }
}
