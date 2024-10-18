import { Component, Inject } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MatDialogModule } from '@angular/material/dialog';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { CommonModule } from '@angular/common'; // Always needed for ngIf, ngFor, etc.

@Component({
  selector: 'app-download-report-dialog',
  standalone: true,
  imports: [
    MatDialogModule,
    MatButtonModule,
    MatIconModule,
    CommonModule
  ],
  templateUrl: './download-report-dialog.component.html',
  styleUrls: ['./download-report-dialog.component.css']
})
export class DownloadReportDialogComponent {
  constructor(
    public dialogRef: MatDialogRef<DownloadReportDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: { reportUrl: string }
  ) {}

  closeDialog(): void {
    this.dialogRef.close();
  }

  downloadReport(): void {
    const link = document.createElement('a');
    link.href = this.data.reportUrl;
    link.download = 'upload_report.log';
    link.click();
  }

  previewReport(): void {
    fetch(this.data.reportUrl)
      .then(response => response.blob())
      .then(blob => {
        const url = URL.createObjectURL(new Blob([blob], { type: 'text/plain' }));
        window.open(url, '_blank');
      })
      .catch(error => {
        console.error('Error fetching and previewing the file:', error);
      });
  }
}
