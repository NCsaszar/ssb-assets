import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpEventType } from '@angular/common/http';

import { FormGroup, FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatSelectModule } from '@angular/material/select';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatButtonModule } from '@angular/material/button';
import { MatProgressBarModule } from '@angular/material/progress-bar';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';

import { BranchService } from '../../../services/branch.service';

@Component({
  selector: 'app-branch-creation-upload',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatDialogModule,
    MatButtonModule,
    MatProgressBarModule,
  ],
  templateUrl: './branch-creation-upload.component.html',
  styleUrl: './branch-creation-upload.component.css'
})
export class BranchCreationUploadComponent {

  selectedFile: any = null;
  selectedFileName: string | null = null;
  uploadForm: FormGroup;
  fileTooLarge = false;
  uploading = false;
  uploadProgress = 0;

  duplicateHandlingOptions = [
    { value: 'overwrite', viewValue: 'Overwrite' },
    { value: 'skip', viewValue: 'Skip' },
    { value: 'duplicate', viewValue: 'Allow Duplicate' }
  ];

  constructor(
    private formBuilder: FormBuilder,
    private branchService: BranchService,
    private snackBar: MatSnackBar,
    private dialog: MatDialog
  ) {
    this.uploadForm = this.formBuilder.group({
      file: ['', Validators.required],
      duplicateHandlingStrategy: ['overwrite', Validators.required]
    });
  }

  onFileSelected(event: any): void {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files.length > 0) {
      this.selectedFile = input.files[0];
      this.fileTooLarge = this.selectedFile.size > 50 * 1024 * 1024; // 50MB

      if (this.fileTooLarge) {
        this.snackBar.open('File size exceeds 50MB limit', 'Close', { duration: 3000 });
        this.selectedFile = null;
        this.uploadForm.reset();
        return;
      }
    
      const fileName = this.selectedFile.name;
      const fileExtension = fileName.split('.').pop().toLowerCase();

      if (fileExtension !== 'csv') {
          this.snackBar.open('Invalid file format. Only CSV files are allowed.', 'Close', {
              duration: 3000,
          });
          this.selectedFile = null;
          this.uploadForm.reset();
          return;
      }

      this.selectedFileName = this.selectedFile.name;
    }
  }

  onSubmit(): void {
    if (!this.selectedFile || this.fileTooLarge) {
      return;
    }

    this.uploading = true;
    const fileReader = new FileReader();
    console.log("uploading");

    fileReader.onload = (e) => {
      const content = fileReader.result?.toString() ?? '';
      const lines = content.split('\n');
    
      console.log('Headers line:', lines[0]);
    
      const headers = lines[0].split(',').map(header => header.trim());
    
      console.log('Processed headers:', headers);
    
      const mandatoryFields = ['Branch Code', 'Branch Name', 'Branch Manager', 'Phone Number', 'Email', 'Address 1', 'Address 2', 'City', 'State', 'Postal Code', 'Country', 'Lat', 'Lng'];
    
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
      formData.append('duplicateHandlingStrategy', this.uploadForm.get('duplicateHandlingStrategy')?.value);
      // formData.append('duplicateHandlingStrategy', this.duplicateHandlingStrategy);

      this.branchService.uploadBranch(formData).subscribe({
        next: (event) => {
          if (event.type === HttpEventType.UploadProgress) {
            this.uploadProgress = Math.round((100 * event.loaded) / event.total);
          } else if (event.type === HttpEventType.Response) {
            this.snackBar.open('File uploaded successfully', 'Close', { duration: 3000 });

            const numRecords = event.body?.numRecords ?? 0;
            this.snackBar.open(`Uploaded ${numRecords} records.`, 'Close', { duration: 6000 });

            this.uploading = false;
            this.resetForm();
          }
        },
        error: (error) => {
          console.error('Failed to upload file', error);
          this.snackBar.open('Failed to upload file. Please try again.', 'Close', { duration: 3000 });
          this.uploading = false;
          this.uploadProgress = 0;
        },
      });
    };

    fileReader.readAsText(this.selectedFile as File);
  }

  resetForm(): void {
    setTimeout(() => {
      this.uploadProgress = 0;
      this.selectedFile = null;
      this.uploadForm.reset();
    }, 3000);
  }

  downloadReport(reportType: string) {
    this.branchService.downloadReport(reportType).subscribe({
      next: (response) => {
        const blob = new Blob([response], { type: 'text/plain' });
        const url = window.URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = url;
        a.download = `${reportType}_report.txt`;
        a.click();
        window.URL.revokeObjectURL(url);
      },
      error: (error) => {
        console.error('Failed to download report', error);
        this.snackBar.open('Failed to download report. Please try again.', 'Close', { duration: 3000 });
      }
    });
  }
}