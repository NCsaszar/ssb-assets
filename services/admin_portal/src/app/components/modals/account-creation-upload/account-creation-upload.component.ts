import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpEventType } from '@angular/common/http';
import { FormGroup, FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { BranchService } from '../../../services/branch.service';
import { AccountsService } from '../../../services/accounts.service';
import { MatSnackBar } from '@angular/material/snack-bar';


@Component({
  selector: 'app-account-creation-upload',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatButtonModule,
  ],
  templateUrl: './account-creation-upload.component.html',
  styleUrl: './account-creation-upload.component.css'
})
export class AccountCreationUploadComponent {

  selectedFile: any = null;
  selectedFileName: string | null = null;
  uploadForm: FormGroup;
  uploading = false;
  uploadProgress = 0;

  constructor(
    private formBuilder: FormBuilder,
    private accountsService: AccountsService,
    private snackBar: MatSnackBar,
  ) {
    this.uploadForm = this.formBuilder.group({
      file: ['', Validators.required]
    });
  }

  onFileSelected(event: any): void {
    this.selectedFile = event.target.files[0] ?? null;
    this.selectedFileName = this.selectedFile?.name ?? null;
  }

  onSubmit() {
    if (!this.selectedFile) {
      return;
    }

    this.uploading = true;

    const fileReader = new FileReader();
    fileReader.onload = (e) => {
      const content = fileReader.result?.toString() ?? '';
      const lines = content.split('\n');

      const headers = lines[0].split(',');
      const mandatoryFields = ['userId', 'accountId', 'accountNumber', 'accountType', 'balance', 'createdAt', 'isActive'];

      for (const field of mandatoryFields) {
        if (!headers.includes(field)) {
          console.error(`Mandatory field '${field}' not found in file.`);
          this.snackBar.open(`Mandatory field '${field}' not found in file.`, 'Close', {
            duration: 3000,
          });
          this.uploading = false;
          return;
        }
      }

      const formData = new FormData();
      formData.append('file', this.selectedFile);

      this.accountsService.uploadAccount(formData).subscribe({  
        next: (event) => {
          if (event.type === HttpEventType.UploadProgress) {
            this.uploadProgress = Math.round((100 * event.loaded) / event.total);
          } else if (event.type === HttpEventType.Response) {
            console.log('File uploaded successfully', event.body);
  
            this.snackBar.open('File uploaded successfully', 'Close', {
              duration: 3000,
            });

            const numRecords = event?.numRecords ?? 0;
            this.snackBar.open(`Uploaded ${numRecords} records.`, 'Close', {
                duration: 6000,
            });
  
            this.uploading = false;
            setTimeout(() => {
              this.uploadProgress = 0;
              this.selectedFile = null;
              this.uploadForm.reset();
            }, 3000);
          }
        },
        error: (error) => {
          console.error('Failed to upload file', error);
          this.snackBar.open('Failed to upload file. Please try again.', 'Close', {
            duration: 3000,
          });
          this.uploading = false;
          this.uploadProgress = 0;
        },
      });
    };

    fileReader.readAsText(this.selectedFile);
  }
}