import { Component, OnInit, Injectable } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import {
  FormBuilder,
  FormsModule,
  Validators,
  ReactiveFormsModule,
  AbstractControl,
  ValidationErrors,
  ValidatorFn,
} from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core';
import { MAT_DATE_LOCALE } from '@angular/material/core';
import { MatSelectModule } from '@angular/material/select';
import { MatGridList, MatGridTile } from '@angular/material/grid-list';
import { MatDivider } from '@angular/material/divider';
import { MatCardModule } from '@angular/material/card';
import { Router } from '@angular/router';
import { User } from '../../../models/user.model';
import { AccountsService } from '../../../services/accounts.service';
import { dateOfBirthValidator } from '../../../utils/validators/dob.validator';
interface Employment {
  value: string;
  viewValue: string;
}

interface AccountType {
  value: string;
  viewValue: string;
}

@Component({
  selector: 'app-open-account-form',
  standalone: true,
  imports: [
    CommonModule,
    MatFormFieldModule,
    MatInputModule,
    FormsModule,
    MatButtonModule,
    MatDatepickerModule,
    MatNativeDateModule,
    MatSelectModule,
    MatGridList,
    MatGridTile,
    MatDivider,
    MatCardModule,
    ReactiveFormsModule,
  ],
  providers: [{ provide: MAT_DATE_LOCALE, useValue: 'en-US' }],
  templateUrl: './open-account-form.component.html',
  styleUrl: './open-account-form.component.css',
})
export class OpenAccountFormComponent implements OnInit {
  constructor(
    private builder: FormBuilder,
    private router: Router,
    private accountsService: AccountsService,
    private _snackBar: MatSnackBar
  ) {}

  users: User[] = [];
  employments: Employment[] = [
    { value: 'Employed', viewValue: 'Employed' },
    { value: 'Un-Employed', viewValue: 'Un-Employed' },
    { value: 'Self-Employed', viewValue: 'Self-Employed' },
    { value: 'Student', viewValue: 'Student' },
    { value: 'Retired', viewValue: 'Retired' },
  ];
  accountTypes: AccountType[] = [
    { value: 'CHECKING', viewValue: 'Checking' },
    { value: 'SAVINGS', viewValue: 'Savings' },
  ];
  startDate = new Date(1990, 0, 1);

  accountForm = this.builder.group({
    accountType: this.builder.control('', Validators.required),
    user: this.builder.control(''),
    firstName: this.builder.control('', Validators.required),
    lastName: this.builder.control('', Validators.required),
    email: this.builder.control('', Validators.required),
    dob: this.builder.control(new Date(), [
      Validators.required,
      dateOfBirthValidator(),
    ]),
    employment: this.builder.control('', Validators.required),
    income: this.builder.control(0, Validators.required),
  });

  fetchUsers(): void {
    this.accountsService.fetchUsers().subscribe({
      next: (users) => {
        this.users = users.content;
      },
      error: (error) => {
        console.error('Error fetching users: ', error);
      },
    });
  }

  createAccount() {
    const formValue = this.accountForm.value;
    if (formValue.accountType && formValue.user) {
      const payload = {
        userId: Number(formValue.user),
        accountType: formValue.accountType,
        initialBalance: 0,
      };

      this.accountsService.createAccount(payload).subscribe({
        next: (response) => {
          this.openSnackBar('Account created successfully', 'Dismiss');
          console.log('Account created successfully', response);
        },
        error: (error) => {
          console.error('Error creating account: ', error);
        },
      });
    }
  }

  cancelAccountCreation() {
    this.router.navigate(['/home']);
  }
  openSnackBar(message: string, action: string) {
    this._snackBar.open(message, action, {
      duration: 5000,
    });
  }

  ngOnInit(): void {
    this.fetchUsers();

    //Subscribe to changes on user form control
    this.accountForm.get('user')?.valueChanges.subscribe((selectedUserId) => {
      const user = this.users.find((u) => u.userId === Number(selectedUserId));
      const dob = user?.dateOfBirth ? new Date(user.dateOfBirth) : null;
      if (user) {
        this.accountForm.patchValue({
          firstName: user.firstName,
          lastName: user.lastName,
          email: user.email,
          dob: dob,
        });
      }
    });
  }
}
