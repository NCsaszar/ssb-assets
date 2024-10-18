import { Component, Inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute } from '@angular/router';

import { MAT_DIALOG_DATA } from '@angular/material/dialog';

import { FormControl, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { BranchService } from '../../../services/branch.service';
import { Branch } from '../../../models/branch.model';

@Component({
  selector: 'app-update-branch',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './update-branch.component.html',
  styleUrl: './update-branch.component.css',
})
export class UpdateBranchComponent {
  constructor(
    @Inject(MAT_DIALOG_DATA) public data: { branchId: number },
    private route: ActivatedRoute,
    private branchService: BranchService
  ) {}

  branch: Branch | undefined;

  updateBranchForm = new FormGroup({
    branchCode: new FormControl(),
    branchName: new FormControl(),
    branchManager: new FormControl(),
    phoneNumber: new FormControl(),
    email: new FormControl(),
    address1: new FormControl(),
    address2: new FormControl(),
    city: new FormControl(),
    state: new FormControl(),
    postalCode: new FormControl(),
    country: new FormControl(),
    lat: new FormControl(),
    lng: new FormControl(),
  });

  updateBranch() {
    const branch: Branch = {
      branchCode: this.updateBranchForm.value.branchCode,
      branchName: this.updateBranchForm.value.branchName,
      branchManager: this.updateBranchForm.value.branchManager,
      phoneNumber: this.updateBranchForm.value.phoneNumber,
      email: this.updateBranchForm.value.email,
      address1: this.updateBranchForm.value.address1,
      address2: this.updateBranchForm.value.address2,
      city: this.updateBranchForm.value.city,
      state: this.updateBranchForm.value.state,
      postalCode: this.updateBranchForm.value.postalCode,
      country: this.updateBranchForm.value.country,
      lat: this.updateBranchForm.value.lat,
      lng: this.updateBranchForm.value.lng,
    };

    console.log('Form Data:', branch);

    this.branchService.updateBranch(branch).subscribe({
      next: (updatedBranch) => {
        console.log('Branch updated:', updatedBranch);
      },
      error: (error) => {
        console.error('Error updating branch:', error);
      },
    });
  }
}
