import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogModule } from '@angular/material/dialog';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { CommonModule } from '@angular/common'; // Always needed for ngIf, ngFor, etc.

@Component({
  selector: 'app-user-upload-error',
  standalone: true,
  imports: [
    MatDialogModule,
    MatButtonModule,
    MatIconModule,
    CommonModule 
  ],
  templateUrl: './user-upload-error.component.html',
  styleUrls: ['./user-upload-error.component.css']
})
export class UserUploadErrorComponent {
  constructor(@Inject(MAT_DIALOG_DATA) public errorMessage: string) {}
}
