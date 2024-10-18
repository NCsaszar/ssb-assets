import { Component, Inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms'; 
import { MAT_DIALOG_DATA, MatDialogRef, MatDialog, MatDialogModule } from '@angular/material/dialog';
import { UserUploadService } from '../../../services/user-upload.service';
import { MatDividerModule } from '@angular/material/divider';
import { MatCardModule } from '@angular/material/card';
import { MatSnackBar } from '@angular/material/snack-bar';
import { SecondaryBtnComponent } from '../../../components/secondary-btn/secondary-btn.component';
import { DownloadReportDialogComponent } from '../download-report-dialog/download-report-dialog.component';

@Component({
  selector: 'app-duplicate-decision-dialog',
  templateUrl: './duplicate-decision-dialog.component.html',
  styleUrls: ['./duplicate-decision-dialog.component.css'],
  standalone: true,
  imports: [
    CommonModule,
    MatDialogModule,
    FormsModule,
    MatCardModule,
    MatDividerModule,
    SecondaryBtnComponent,
  ]
})
export class DuplicateDecisionDialogComponent implements OnInit {
  decisions: any[] = [];

  constructor(
    public dialogRef: MatDialogRef<DuplicateDecisionDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: { duplicates: any[] },
    private userUploadService: UserUploadService,
    private snackBar: MatSnackBar,
    private dialog: MatDialog,
  ) { }

  ngOnInit(): void {
    this.decisions = this.data.duplicates.map(duplicate => ({
      username: duplicate.username,
      decision: duplicate.decision || 'SKIP'
    }));
  }

  trackByFn(index: any, item: { username: any; }) {
    return item.username;
  }

  closeDialog(): void {
    this.dialogRef.close(this.decisions);
  }

  resolveDuplicates(): void {
    this.userUploadService.resolveDuplicates(this.decisions).subscribe({
      next: () => {
        this.snackBar.open('Duplicates resolved successfully', '', {
          duration: 2000
        });
        this.dialogRef.close();
        this.openDownloadReportDialog();
      },
      error: (error: { message: string; }) => {
        this.snackBar.open('Failed to resolve duplicates: ' + error.message);
      }
    });
  }

  openDownloadReportDialog(): void {
    this.userUploadService.downloadLogFile().subscribe({
      next: (blob) => {
        const url = window.URL.createObjectURL(blob);
        this.dialog.open(DownloadReportDialogComponent, {
          data: { reportUrl: url }
        });
      },
      error: (error) => {
        this.snackBar.open('Failed to download report: ' + error.message, '', {
          duration: 3000
        });
      }
    });
  }

}
