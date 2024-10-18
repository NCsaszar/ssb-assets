import { Component, ViewChild, ElementRef, OnInit } from "@angular/core";
import { CommonModule } from '@angular/common';
import { MatIconModule } from '@angular/material/icon';
import { MatDialogModule, MatDialog } from '@angular/material/dialog';
import { UserUploadService } from '../../services/user-upload.service';
import { ProgressComponent } from './progress/progress.component';
import { UserUploadErrorComponent } from './user-upload-error/user-upload-error.component';
import { DuplicateDecisionDialogComponent } from './duplicate-decision-dialog/duplicate-decision-dialog.component';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-user-upload',
  standalone: true,
  imports: [
    CommonModule,
    MatIconModule,
    ProgressComponent,
    MatDialogModule,
  ],
  templateUrl: './user-upload.component.html',
  styleUrls: ['./user-upload.component.css']
})
export class UserUploadComponent implements OnInit {
  @ViewChild("fileDropRef", { static: false }) fileDropEl!: ElementRef;
  files: { file: File; progress: number; }[] = [];

  constructor(
    private userUploadService: UserUploadService,
    private dialog: MatDialog,
    private snackBar: MatSnackBar,
  ) { }

  ngOnInit() {
    this.userUploadService.duplicateStatus$.subscribe(duplicateStatus => {
      if (duplicateStatus) {
        this.checkForPendingDuplicates();
      }
    });
  }

  onFileDropped(event: DragEvent) {
    event.preventDefault(); //prevent opening the file in a browser
    if (event.dataTransfer && event.dataTransfer.files) {
      const fileArray = Array.from(event.dataTransfer.files);
      this.prepareFilesList(fileArray);
    }
  }

  onDragOver(event: DragEvent) {
    event.preventDefault();
  }

  fileBrowseHandler(event: Event) {
    const inputElement = event.target as HTMLInputElement;
    if (inputElement && inputElement.files) {
      const fileArray = Array.from(inputElement.files);
      this.prepareFilesList(fileArray);
    }
  }

  prepareFilesList(files: File[]) {
    files.forEach(file => {
      if (this.validateFile(file)) {
        this.files.push({ file: file, progress: 0 });
        this.uploadFile(file);
      } else {
        this.dialog.open(UserUploadErrorComponent, {
          data: `Invalid file: ${file.name}. File must be a CSV and less than 50MB.`
        });
      }
    });
    this.fileDropEl.nativeElement.value = ""; // Reset the input
  }

  validateFile(file: File): boolean {
    const MAX_SIZE = 50 * 1024 * 1024; // 50MB in bytes
    const ALLOWED_TYPE = 'text/csv';
    return file.size <= MAX_SIZE && file.type === ALLOWED_TYPE;
  }

  uploadFile(file: File) {
    this.userUploadService.uploadFile(file).subscribe({
      next: (response) => {
        this.snackBar.open('Data upload initiated', '', {
          duration: 2000
        });
        const index = this.files.findIndex(f => f.file === file);
        if (index !== -1) { //file was found in the array
          this.files[index].progress = 100;
        }
      },
      error: (error) => {
        if (error.status === 400 && error.error.errors) {
          const validationErrors = error.error.errors;
          const errorMessage = `Validation errors:\n${validationErrors.join('\n')}`;
          this.dialog.open(UserUploadErrorComponent, { data: errorMessage });
        } else {
          this.dialog.open(UserUploadErrorComponent, { data: 'Error uploading file: ' + (error.message || 'Unknown error') });
        }
      }
    });
  }

  checkForPendingDuplicates() {
    this.userUploadService.getPendingDuplicates().subscribe({
      next: (duplicates) => {
        if (duplicates && duplicates.length > 0) {
          duplicates.sort((a, b) => a.username.localeCompare(b.username));
          this.openDuplicateDialog(duplicates);
        } else {
          this.snackBar.open('No duplicates to process.');
        }
      },
      error: (error) => {
        this.dialog.open(UserUploadErrorComponent, {
          data: 'Error fetching duplicates: ' + (error.error?.message || error.message)
        });
      }
    });
  }
  

  openDuplicateDialog(duplicates: any[]) {
    const dialogRef = this.dialog.open(DuplicateDecisionDialogComponent, {
      data: { duplicates }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result && result.length > 0) {
        this.userUploadService.resolveDuplicates(result).subscribe({
          next: () => {
            this.snackBar.open('Duplicates resolved successfully', '', {
              duration: 3000
            });
          },
          error: (error) => {
            this.snackBar.open('Failed to resolve duplicates: ' + error.message, '', {
              duration: 3000
            });
          }
        });
      }
    });
  }
  openCsvFile(file: File) {
    const fileReader = new FileReader();
    fileReader.onload = (event) => {
      const csvData = event.target?.result as string;
      const htmlContent = this.generateHtmlTable(csvData);
      const newWindow = window.open("", "_blank");
      if (newWindow) {
        newWindow.document.write(htmlContent);
        newWindow.document.close();
      }
    };
    fileReader.readAsText(file);
  }
  
  generateHtmlTable(csvData: string): string {
    const rows = csvData.split("\n");
    let table = "<table border='1'><thead><tr>";
    const headers = rows[0].split(",");
    headers.forEach(header => {
      table += `<th>${header.trim()}</th>`;
    });
    table += "</tr></thead><tbody>";
    for (let i = 1; i < rows.length; i++) {
      if (rows[i].trim()) {
        const cells = rows[i].split(",");
        table += "<tr>";
        cells.forEach(cell => {
          table += `<td>${cell.trim()}</td>`;
        });
        table += "</tr>";
      }
    }
    table += "</tbody></table>";
    return `
      <html>
        <head>
          <title>CSV Preview</title>
          <style>
            table {
              width: 100%;
              border-collapse: collapse;
            }
            th, td {
              padding: 8px;
              text-align: left;
              border-bottom: 1px solid #ddd;
            }
            th {
              background-color: #f2f2f2;
            }
            tr:hover {
              background-color: #f5f5f5;
            }
          </style>
        </head>
        <body>${table}</body>
      </html>`;
  }

  formatBytes(bytes: number, decimals = 2): string {
    if (bytes === 0) {
      return "0 Bytes";
    }
    const k = 1024;
    const dm = decimals < 0 ? 0 : decimals;
    const sizes = ["Bytes", "KB", "MB"];
    const i = Math.floor(Math.log(bytes) / Math.log(k));
    return parseFloat((bytes / Math.pow(k, i)).toFixed(dm)) + " " + sizes[i];
  }
}
