import { Component, Injectable, OnInit, ViewChild } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { MatTableModule, MatTableDataSource } from '@angular/material/table';
import {MatButtonModule} from '@angular/material/button';
import { MatDialog, MatDialogRef } from '@angular/material/dialog';
import {MatIconModule} from '@angular/material/icon';
import { FormsModule } from '@angular/forms';
import { EditCreditOfferModalComponent } from '../../../components/modals/edit-credit-offer-modal/edit-credit-offer-modal.component';
import { MatCardModule } from '@angular/material/card';
import { MatDividerModule } from '@angular/material/divider';
import { MatPaginator } from '@angular/material/paginator';
import { CookieService } from 'ngx-cookie-service';

@Component({
  standalone: true,
  selector: 'app-credit-offers',
  imports:[CommonModule, MatTableModule,FormsModule, MatIconModule,MatButtonModule,MatCardModule,MatDividerModule, MatPaginator],
  templateUrl: './credit-offers.component.html',
  styleUrls: ['./credit-offers.component.css']
})
@Injectable()
export class CreditOffersComponent implements OnInit {
  creditOffers: MatTableDataSource<any> = new MatTableDataSource<any>();

  @ViewChild(MatPaginator) paginator!: MatPaginator;

  constructor(private http: HttpClient, private dialog: MatDialog, private cookieService: CookieService) { }

  openCreateModal(): void {
    const dialogRef = this.dialog.open(EditCreditOfferModalComponent, {
      width: '500px',
      data: { offer: { cardOfferId: null, cardOfferName: '', apr: 0, creditLimit: 0, cardOfferStatus: false } }
    });

    dialogRef.afterClosed().subscribe(result => {
      console.log('The dialog was closed');
    });
  }

  ngOnInit(): void {
    this.fetchCreditOffers();
  }

  fetchCreditOffers(): void {
    const headers = new HttpHeaders().set('Authorization', 'Bearer ' + this.cookieService.get('token'));

    const creditLimit = 9999999;

    this.http.get<any[]>('/api/v1/cards/credit-offer', {
      headers,
      params: { creditLimit: creditLimit.toString() }
    }).subscribe({
      next: (response) => {
        this.creditOffers = new MatTableDataSource(response);
        this.creditOffers.paginator = this.paginator;
      },
      error: (error) => {
        console.error('Error fetching credit offers:', error);
      }
    });
  }

  openEditModal(offer: any): void {
  const offerData = offer ? offer : { cardOfferName: '', apr: 0, creditLimit: 0, cardOfferStatus: false };
  const dialogRef = this.dialog.open(EditCreditOfferModalComponent, {
    width: '500px',
    data: { offer: offerData }
  });

  dialogRef.afterClosed().subscribe(result => {
    console.log('The dialog was closed');
  });
}
}
