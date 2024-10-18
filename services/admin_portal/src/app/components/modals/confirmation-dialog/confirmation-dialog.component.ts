// confirmation-dialog.component.ts
import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef, MatDialogModule } from '@angular/material/dialog';
// MAT_DIALOG_DATA is a token used to inject data into the dialog, MatDialogRef provides a reference to the dialog opened,
// and MatDialogModule is the module that contains dialog-related components and services.
import { FormsModule } from '@angular/forms'; //allows to use features such as ngModel for two-way data binding in forms.
import { PrimaryBtnComponent } from '../../primary-btn/primary-btn.component';
import { SecondaryBtnComponent } from '../../secondary-btn/secondary-btn.component';
import { MatFormFieldModule } from '@angular/material/form-field';

@Component({
  selector: 'app-confirmation-dialog',
  standalone: true,
  imports: [FormsModule, MatDialogModule, PrimaryBtnComponent,
    SecondaryBtnComponent, MatFormFieldModule],
  templateUrl: './confirmation-dialog.component.html',
  styleUrls: ['./confirmation-dialog.component.css'],
})
export class ConfirmationDialogComponent {
  password: any = '';

  constructor(
    public dialogRef: MatDialogRef<ConfirmationDialogComponent>, //Injects a reference to the dialog containing this component, allowing the component to close the dialog and optionally return data.
    @Inject(MAT_DIALOG_DATA) public data: any) { } // Injects any data passed to this dialog, allowing the component to access external data provided at the time of opening the dialog.

  onConfirm(): void {
    this.dialogRef.close(this.password);
  }

  onCancel(): void {
    this.dialogRef.close();
  }
}
