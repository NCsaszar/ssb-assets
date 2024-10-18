import { Component, Injectable, OnInit, ViewChild } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { MatButtonModule } from '@angular/material/button';
import { MatDialog } from '@angular/material/dialog';
import { MatIconModule } from '@angular/material/icon';
import { FormsModule } from '@angular/forms';
import { EditLoanOfferModalComponent } from '../../../components/modals/edit-credit-offer-modal/edit-loan-offer-modal/edit-loan-offer-modal.component';
import { CreateLoanOfferModalComponent } from '../../../components/modals/create-loan-offer-modal/create-loan-offer-modal.component';
import { MatPaginator } from '@angular/material/paginator';
import { MatCardModule } from '@angular/material/card';
import {  MatDividerModule } from '@angular/material/divider';
import { MatFormFieldModule, MatLabel } from '@angular/material/form-field';
import { CookieService } from 'ngx-cookie-service';

@Component({
  standalone: true,
  selector: 'app-loan-offers',
  imports: [CommonModule, MatTableModule, FormsModule, MatIconModule, MatButtonModule, MatPaginator,MatCardModule,MatDividerModule,MatFormFieldModule, MatLabel],
  templateUrl: './loan-offers.component.html',
  styleUrls: ['./loan-offers.component.css']
})
export class LoanOffersComponent implements OnInit {
  loanOffers: MatTableDataSource<any> = new MatTableDataSource<any>();
  @ViewChild(MatPaginator) paginator!: MatPaginator;

  constructor(private http: HttpClient, private dialog: MatDialog, private cookieService: CookieService) { }

  ngOnInit(): void {
    this.fetchLoanOffers();
  }

  fetchLoanOffers(): void {
    const headers = new HttpHeaders().set('Authorization', 'Bearer ' + this.cookieService.get('token'));

    this.http.get<any[]>('/api/v1/loans/loan', { headers }).subscribe({
      next: (response) => {
        this.loanOffers = new MatTableDataSource(response);
        this.loanOffers.paginator = this.paginator;
      },
      error: (error) => {
        console.error('Error fetching loan offers:', error);
      }
    });
  }

  openCreateModal(): void {
    const dialogRef = this.dialog.open(CreateLoanOfferModalComponent, {
      width: '500px',
    });
  }

  openEditModal(offer: any): void {
    const dialogRef = this.dialog.open(EditLoanOfferModalComponent, {
      width: '500px',
      data: { offer: offer }
    });

    dialogRef.afterClosed().subscribe(result => {
      console.log('The dialog was closed');
    });
  }

  applyFilter(event: any) {
    const filterValue = event.target.value;
    this.loanOffers.filter = filterValue.trim().toLowerCase();
  }
}
