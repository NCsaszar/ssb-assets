import { Component, Inject } from '@angular/core';
import { MatDialog, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatDividerModule } from '@angular/material/divider';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSlideToggleModule } from '@angular/material/slide-toggle';
import { CookieService } from 'ngx-cookie-service';
import { SecondaryBtnComponent } from '../../../secondary-btn/secondary-btn.component';

@Component({
  standalone: true,
  imports:[FormsModule,CommonModule,MatCardModule, MatDividerModule,MatInputModule,MatFormFieldModule,MatSlideToggleModule, SecondaryBtnComponent ],
  selector: 'app-edit-loan-offer-modal',
  templateUrl: './edit-loan-offer-modal.component.html',
  styleUrls: ['./edit-loan-offer-modal.component.css']
})
export class EditLoanOfferModalComponent {
  isEditing: boolean = false;
  loanTypes: any[] = [
    { loanTypeName: 'Personal Loans' },
    { loanTypeName: 'Mortgage' },
    { loanTypeName: 'Student Loans' },
    { loanTypeName: 'Auto Loans' }
  ];

  constructor(
    private http: HttpClient,
    public dialogRef: MatDialogRef<EditLoanOfferModalComponent>,
    private cookieService: CookieService,
    @Inject(MAT_DIALOG_DATA) public data: any
  ) {
    this.isEditing = !!(data && data.offer && data.offer.loanID);
  }

  onCancelClick(): void {
    this.dialogRef.close();
  }

  onSubmit(): void {
    const updateData = this.data.offer;
    const endpoint = 'http://localhost:8765/CARDSLOANS-SERVICE/api/v1/loans/loan';
  
    const headers = new HttpHeaders().set('Authorization', 'Bearer ' + this.cookieService.get('token'));;
  
    this.http.put(endpoint, updateData, { headers, params: { loanID: this.data.offer.loanID } }).subscribe({
      next: (response: any) => {
        console.log('Response from server:', response);
        if (response === 'Loan Updated successfully') {
          console.log('Loan updated successfully');
          this.dialogRef.close();
        } else {
          console.error('Unexpected response from server:', response);
          this.dialogRef.close();
        }
      },
      error: (error: any) => {
        console.error('Error updating loan:', error);
        this.dialogRef.close();
      }
    });
  }
}