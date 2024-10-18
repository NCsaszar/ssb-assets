import { Component, Inject } from '@angular/core';
import { MatDialog, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { FormGroup, FormControl, Validators, FormBuilder,ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldControl, MatFormFieldModule } from '@angular/material/form-field';
import { MatDividerModule } from '@angular/material/divider';
import { CookieService } from 'ngx-cookie-service';

@Component({
  standalone: true,
  imports:[FormsModule,CommonModule, ReactiveFormsModule,MatCardModule,MatFormFieldModule,MatDividerModule, ],
  selector: 'app-create-loan-offer-modal',
  templateUrl: './create-loan-offer-modal.component.html',
  styleUrls: ['./create-loan-offer-modal.component.css']
})
export class CreateLoanOfferModalComponent {
    loanTypes: any[] = [
      { loanTypeId: 1, loanTypeName: 'Personal Loans' },
      { loanTypeId: 2, loanTypeName: 'Mortgage' },
      { loanTypeId: 3, loanTypeName: 'Student Loans' },
      { loanTypeId: 4, loanTypeName: 'Auto Loans' }
    ];

    loanOfferForm: FormGroup;

    constructor(
      private http: HttpClient,
      public dialogRef: MatDialogRef<CreateLoanOfferModalComponent>,
      @Inject(MAT_DIALOG_DATA) public data: any,
      private formBuilder: FormBuilder,
      private cookieService: CookieService
    ) {
      this.data = this.data || { offer: { loanType: {} } };

      this.loanOfferForm = this.formBuilder.group({
        annualPercentageRate: [null, Validators.required],
        termMonths: [null, Validators.required],
        maxAmount: [null, Validators.required],
        minAmount: [null, Validators.required],
        loanStatus: [false],
        loanType: [null, Validators.required]
      });
    }

    onCancelClick(): void {
      this.dialogRef.close();
    }

    onSubmit(): void {
      if (this.loanOfferForm.valid) {
        const formData = this.loanOfferForm.value;
        formData.loanType = {
          loanTypeId: formData.loanType.loanTypeId,
          loanTypeName: formData.loanType.loanTypeName
        };
        console.log('Data to be submitted:', formData);
        const endpoint = '/api/v1/loans/loan';
        const headers = new HttpHeaders().set('Authorization', 'Bearer ' + this.cookieService.get('token'));;

        this.http.post(endpoint, formData, { headers, responseType: 'json' }).subscribe({
          next: (response: any) => {
            console.log('Response from server:', response);

            if (response === 'Loan Created') {
              console.log('Loan offer created successfully');
              this.dialogRef.close();
            } else {
              console.error('Unexpected response from server:', response);
              this.dialogRef.close();
            }
          },
          error: (error: any) => {
            console.error('Error creating loan offer:', error);
          }
        });
      } else {
        console.error('Form is invalid. Please fill all required fields correctly.');
      }
    }

    onLoanTypeChange(selectedLoanType: any): void {
      this.data.offer.loanType = selectedLoanType;
    }
  }
