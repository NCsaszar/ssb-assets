import { Component, Inject } from '@angular/core';
import { MatDialog, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { MatDividerModule } from '@angular/material/divider';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule, MatLabel } from '@angular/material/form-field';
import { CommonModule } from '@angular/common';
import { MatSlideToggleModule } from '@angular/material/slide-toggle';
import { SecondaryBtnComponent } from '../../secondary-btn/secondary-btn.component';
import { MatInputModule } from '@angular/material/input';
import { CookieService } from 'ngx-cookie-service';

@Component({
  standalone: true,
  imports:[FormsModule, MatDividerModule, MatCardModule, MatFormFieldModule, CommonModule, MatSlideToggleModule, MatLabel, SecondaryBtnComponent, MatFormFieldModule,FormsModule, MatInputModule],
  selector: 'app-edit-credit-offer-modal',
  templateUrl: './edit-credit-offer-modal.component.html',
  styleUrls: ['./edit-credit-offer-modal.component.css']
})
export class EditCreditOfferModalComponent {

  constructor(
    private http: HttpClient,
    private cookieService: CookieService,
    public dialogRef: MatDialogRef<EditCreditOfferModalComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any
  ) {}

  onCancelClick(): void {
    this.dialogRef.close();
  }

  onSubmit(): void {
    const headers = new HttpHeaders().set('Authorization', 'Bearer ' + this.cookieService.get('token'));
    let endpoint = '/api/v1/cards/credit-offer';
    let requestType = 'POST';

    if (this.data.offer.cardOfferId) {
      endpoint += `?cardOfferId=${this.data.offer.cardOfferId}`;
      requestType = 'PUT';
    }

    this.http.request(requestType, endpoint, {
      headers: headers,
      body: {
        cardOfferName: this.data.offer.cardOfferName,
        apr: this.data.offer.apr,
        creditLimit: this.data.offer.creditLimit,
        cardOfferStatus: this.data.offer.cardOfferStatus
      }
    }).subscribe({
      next: (response) => {
        console.log('Credit offer', requestType === 'PUT' ? 'updated' : 'created', 'successfully:', response);
        this.dialogRef.close();
      },
      error: (error) => {
        console.error('Error', requestType === 'PUT' ? 'updating' : 'creating', 'credit offer:', error);
        this.dialogRef.close();
      }
    });
  }

}
