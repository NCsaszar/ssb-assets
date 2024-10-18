import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { FormControl, FormGroup, FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { MatButtonModule } from '@angular/material/button';
import { BranchService } from '../../../services/branch.service';
import { Branch } from '../../../models/branch.model';

@Component({
  selector: 'app-create-branch',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatDialogModule,
    MatButtonModule,
  ],
  templateUrl: './create-branch.component.html',
  styleUrl: './create-branch.component.css',
})
export class CreateBranchComponent {

  createBranchForm: FormGroup;
  branch: Branch | undefined;

  constructor(
    private route: ActivatedRoute,
    private branchService: BranchService,
    private formBuilder: FormBuilder
  ) {
    this.createBranchForm = this.formBuilder.group({
      branchCode: ['', [Validators.required, Validators.pattern('SSB[1234567890][1234567890][1234567890]')]],
      branchName: ['', Validators.required],
      branchManager: ['', Validators.required],
      phoneNumber: ['', [Validators.required, Validators.pattern('\\d{3}-\\d{3}-\\d{4}')]],
      email: ['', [Validators.required, Validators.email]],
      address1: ['', Validators.required],
      address2: [''],
      city: ['', Validators.required],
      state: ['', Validators.required],
      postalCode: ['', Validators.required],
      country: ['', Validators.required],
      lat: ['', Validators.required],
      lng: ['', Validators.required],
    });
  }

  createBranch() {
    if (this.createBranchForm.invalid) {
      return;
    }

    const branch: Branch = {
      branchCode: this.createBranchForm.value.branchCode,
      branchName: this.createBranchForm.value.branchName,
      branchManager: this.createBranchForm.value.branchManager,
      phoneNumber: this.createBranchForm.value.phoneNumber,
      email: this.createBranchForm.value.email,
      address1: this.createBranchForm.value.address1,
      address2: this.createBranchForm.value.address2,
      city: this.createBranchForm.value.city,
      state: this.createBranchForm.value.state,
      postalCode: this.createBranchForm.value.postalCode,
      country: this.createBranchForm.value.country,
      lat: this.createBranchForm.value.lat,
      lng: this.createBranchForm.value.lng,
    };

    console.log('Branch form Data:', branch);

    this.branchService.createBranch(branch).subscribe({
      next: (createdBranch) => {
        console.log('New branch created:', createdBranch);
      },
      error: (error) => {
        console.error('Error creating branch:', error);
      },
    });
  }
}