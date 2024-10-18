import { Component, Inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute } from '@angular/router';

import { MAT_DIALOG_DATA } from '@angular/material/dialog';

import { FormControl, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { BranchService } from '../../../services/branch.service';
import { Branch } from '../../../models/branch.model';
import { BranchLocation } from '../../../models/branch-location.model';

@Component({
  selector: 'app-update-branch-location',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './update-branch-location.component.html',
  styleUrl: './update-branch-location.component.css',
})
export class UpdateBranchLocationComponent {
  constructor(
    @Inject(MAT_DIALOG_DATA) public data: { branchId: number },
    private route: ActivatedRoute,
    private branchService: BranchService
  ) {}

  branch: Branch | undefined;
  branchLocation: BranchLocation | undefined;

  updateBranchLocationForm = new FormGroup({
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
    const branchId = this.data.branchId;
    const location: Partial<BranchLocation> = {
      address1: this.updateBranchLocationForm.value.address1,
      address2: this.updateBranchLocationForm.value.address2,
      city: this.updateBranchLocationForm.value.city,
      state: this.updateBranchLocationForm.value.state,
      postalCode: this.updateBranchLocationForm.value.postalCode,
      country: this.updateBranchLocationForm.value.country,
      lat: this.updateBranchLocationForm.value.lat,
      lng: this.updateBranchLocationForm.value.lng,
    };

    console.log('Form Data:', location);

    this.branchService.updateBranchLocation(branchId, location).subscribe({
      next: (updatedBranch) => {
        console.log('Branch location updated:', updatedBranch);
      },
      error: (error) => {
        console.error('Error updating branch:', error);
      },
    });
  }
}
