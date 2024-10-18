import { Component } from '@angular/core';
import { MatDialogModule } from '@angular/material/dialog';
import { MatButtonModule } from '@angular/material/button';

@Component({
  selector: 'app-confirm-reversal-modal',
  standalone: true,
  imports: [MatDialogModule, MatButtonModule],
  templateUrl: './confirm-reversal-modal.component.html',
  styleUrl: './confirm-reversal-modal.component.css',
})
export class ConfirmReversalModalComponent {}
