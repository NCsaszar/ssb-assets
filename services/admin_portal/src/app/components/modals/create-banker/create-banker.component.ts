import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { FormControl, FormGroup, FormBuilder, ReactiveFormsModule, Validators, Form } from '@angular/forms';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { MatButtonModule } from '@angular/material/button';
import { BankerService } from '../../../services/banker.service';
import { Banker } from '../../../models/banker.model';

@Component({
  selector: 'app-create-banker',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatDialogModule,
    MatButtonModule,
  ],
  templateUrl: './create-banker.component.html',
  styleUrl: './create-banker.component.css',
})
export class CreateBankerComponent {

  createBankerForm: FormGroup;
  banker: Banker | undefined;

  constructor(
    private route: ActivatedRoute,
    private bankerService: BankerService,
    private formBuilder: FormBuilder
  ) {
    this.createBankerForm = this.formBuilder.group({
      firstName: ['', Validators.required],
      lastName: ['', Validators.required],
      phoneNumber: ['', [Validators.required, Validators.pattern('\\d{3}-\\d{3}-\\d{4}')]],
      email: ['', [Validators.required, Validators.email]],
      jobTitle: ['', Validators.required],
    })
  }

  createBanker() {
    if (this.createBankerForm.invalid) {
      return;
    }
    
    const banker: Banker = {
      firstName: this.createBankerForm.value.firstName,
      lastName: this.createBankerForm.value.lastName,
      phoneNumber: this.createBankerForm.value.phoneNumber,
      email: this.createBankerForm.value.email,
      jobTitle: this.createBankerForm.value.jobTitle,
    };

    console.log('Banker form Data:', banker);

    this.bankerService.createBanker(banker).subscribe({
      next: (createdBanker) => {
        console.log('New banker added:', createdBanker);
      },
      error: (error) => {
        console.error('Error creating banker:', error);
      },
    });
  }
}
