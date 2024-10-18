import {
  Component,
  Input,
  OnChanges,
  SimpleChanges,
  Output,
  EventEmitter,
  ChangeDetectorRef,
  OnInit,
} from '@angular/core';
import {
  FormGroup,
  FormBuilder,
  Validators,
  ReactiveFormsModule,
} from '@angular/forms';
import { CommonModule } from '@angular/common';
import { User } from '../../../models/user.model';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { CookieService } from 'ngx-cookie-service';
import { HttpClient, HttpHeaders, HttpErrorResponse } from '@angular/common/http';
import { ConfirmationDialogComponent } from '../confirmation-dialog/confirmation-dialog.component';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MatSlideToggleModule } from '@angular/material/slide-toggle';
import { MatIconModule } from '@angular/material/icon';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatNativeDateModule } from '@angular/material/core';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatCardModule } from '@angular/material/card';
import { MatDividerModule } from '@angular/material/divider';
import { MatSelectModule } from '@angular/material/select';
import { SecondaryBtnComponent } from '../../secondary-btn/secondary-btn.component';
import {
  dateOfBirthValidator,
  getMaxDate,
} from '../../../utils/validators/dob.validator';
import { API_CONFIG } from '../../../utils/config/apiConfig';
import { getUsername } from '../../../utils/token-utils';

@Component({
  selector: 'app-user-details-modal',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    ConfirmationDialogComponent,
    MatDialogModule,
    MatSlideToggleModule,
    MatIconModule,
    MatFormFieldModule,
    MatInputModule,
    MatNativeDateModule,
    MatDatepickerModule,
    MatCardModule,
    MatDividerModule,
    MatSelectModule,
    SecondaryBtnComponent
  ],
  templateUrl: './user-details-modal.component.html',
  styleUrls: ['./user-details-modal.component.css'],
})
export class UserDetailsModalComponent implements OnChanges, OnInit {
  @Input() user!: User; // Use '!' to assure TypeScript that 'user' will be initialized.
  @Input() show: boolean = false; // Controls the visibility of the modal.
  @Output() closeEvent = new EventEmitter<User | 'deleted' | null>(); // Emits an event when the modal is requested to close.
  @Input() key!: number;

  secretQuestions = [
    "What is your mother's maiden name?",
    'What was the name of your first pet?',
    'What was the make of your first car?',
    'Where did you go to high school/college?',
    'What is your favorite food?',
    'What is your favorite color?',
    'What is your favorite movie?',
    'What is your favorite book?',
    'What is your favorite sport?',
    'What is your favorite holiday?',
    'What is your favorite song?',
    'What is your favorite hobby?',
    'What is your favorite animal?',
  ];
  userForm: FormGroup; // FormGroup to manage user details form.
  private updatedUser: User | null = null;
  public getMaxDate = getMaxDate;

  constructor(
    private http: HttpClient,
    private dialog: MatDialog,
    private fb: FormBuilder,
    private cdr: ChangeDetectorRef,
    private snackBar: MatSnackBar,
    private cookieService: CookieService
  ) {
    this.userForm = this.fb.group({
      userId: [{ value: '', disabled: true }], // userId is view-only
      username: ['', Validators.required],
      firstName: ['', Validators.required],
      lastName: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      address: ['', Validators.required],
      dateOfBirth: ['', [Validators.required, dateOfBirthValidator()]],
      phoneNumber: ['', [Validators.required]],
      secretQuestion: ['', Validators.required],
      secretAnswer: ['', Validators.required],
      isActive: [false], // Default value is false
    });
  }

  ngOnInit(): void {
    this.patchFormValues(this.user);
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['user'] && changes['user'].currentValue) {
      this.patchFormValues(changes['user'].currentValue);
    }
  }

  private patchFormValues(user: User): void {
    if (user) {
      this.userForm.patchValue({
        ...user,
        dateOfBirth: user.dateOfBirth ? new Date(user.dateOfBirth) : null,
      });
    }
  }

  onSubmit(): void {
    if (this.userForm.valid) {
      const headers = new HttpHeaders({
        Authorization: `Bearer ${this.cookieService.get('token')}`,
      });

      this.http.patch<User>(
        `${API_CONFIG.updateUserDetailsEndpoint}/${this.user.userId}`,
        this.userForm.value,
        { headers }
      ).subscribe({
        next: (user) => {
          this.snackBar.open('User updated successfully', '', {
            duration: 2000,
          });
          this.updatedUser = user; // Store the updated user
          this.close();
        },
        error: (error) => {
          let errorMessage = 'There was an error updating the user details.';
          if (error.status === 409) {
            //The HTTP 409 Conflict response status code indicates a request conflict with the current state of the target resource.
            errorMessage = 'Please make sure that user credentials are unique.';
          }
          this.snackBar.open(errorMessage, '', { duration: 2000 });
        },
      });
    } else {
      this.snackBar.open(
        'There is an error with the form inputs. Please review and correct them.',
        '',
        { duration: 3000 }
      );
    }
  }

  confirmDelete(): void {
    const dialogRef = this.dialog.open(ConfirmationDialogComponent, {
      width: '350px',
      data: {},
    });

    dialogRef.afterClosed().subscribe((password) => {
      if (password) {
        this.deleteUser(password);
      }
    });
  }

  async deleteUser(password: string): Promise<void> {
    const headers = new HttpHeaders({
      Authorization: `Bearer ${this.cookieService.get('token')}`,
    });
    const currentUsername = getUsername(this.cookieService);

    try {
      // Verify the password first
      const verificationResponse = await this.http.post<{ status: string }>(
        `${API_CONFIG.authenticateEndpoint}`,
        { username: currentUsername, password: password },
        { headers }
      ).toPromise();

      // If verification is successful, proceed to delete the user
      await this.http.delete(
        `${API_CONFIG.deleteUserEndpoint}/${this.user.userId}`,
        { headers, responseType: 'text' }
      ).toPromise();

      this.snackBar.open('User deleted successfully', 'Close', {
        duration: 2000,
      });
      this.closeEvent.emit('deleted'); // Signal successful deletion to refresh the list
    } catch (error: any) { // Use 'any' to avoid explicit casting later
      console.error('Error deleting user', error);

      // Default error message
      let errorMessage = 'Error deleting user. Please ensure the password is correct and try again.';

      if (error instanceof HttpErrorResponse) {
        // Handle 401 Unauthorized specifically
        if (error.status === 401) {
          errorMessage = 'Authentication failed: Invalid username or password.';
        } else {
          // Attempt to use a more specific error message if available
          errorMessage = `Error deleting user. ${(error.error?.message || error.message || error.error || errorMessage)}`;
        }
      } else if (typeof error === 'string') {
        // Handle the case where error might directly be a string
        errorMessage = `Error deleting user. ${error}`;
      }

      this.snackBar.open(errorMessage, 'Close', { duration: 3000 });
    }
  }

  close() {
    // Emit the stored updated user or null if no update was performed
    this.closeEvent.emit(this.updatedUser);
    this.show = false;
    // Reset the updatedUser to null for next use
    this.updatedUser = null;
  }
}
