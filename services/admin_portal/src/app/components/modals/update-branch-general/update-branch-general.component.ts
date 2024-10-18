import { Component, Inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute } from '@angular/router';

import { MAT_DIALOG_DATA } from '@angular/material/dialog';

import { FormControl, FormGroup, FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { BranchService } from '../../../services/branch.service';
import { Branch } from '../../../models/branch.model';
import { BranchGeneral } from '../../../models/branch-general.model';

@Component({
  selector: 'app-update-branch-general',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './update-branch-general.component.html',
  styleUrl: './update-branch-general.component.css',
})
export class UpdateBranchGeneralComponent {

  updateBranchGeneralForm: FormGroup;
  branch: Branch | undefined;
  branchGeneral: BranchGeneral | undefined;

  constructor(
    @Inject(MAT_DIALOG_DATA) public data: { branchId: number },
    private route: ActivatedRoute,
    private branchService: BranchService,
    private formBuilder: FormBuilder
  ) {
    this.updateBranchGeneralForm = this.formBuilder.group({
      branchCode: ['', [Validators.required, Validators.pattern('SSB[1234567890][1234567890][1234567890]')]],
      branchName: ['', Validators.required],
      branchManager: ['', Validators.required],
      phoneNumber: ['', [Validators.required, Validators.pattern('\\d{3}-\\d{3}-\\d{4}')]],
      email: ['', [Validators.required, Validators.email]],
    });
  }

  updateBranch() {
    if (this.updateBranchGeneralForm.invalid) {
      return;
    }

    const branchId = this.data.branchId;
    const general: Partial<BranchGeneral> = {
      branchCode: this.updateBranchGeneralForm.value.branchCode,
      branchName: this.updateBranchGeneralForm.value.branchName,
      branchManager: this.updateBranchGeneralForm.value.branchManager,
      phoneNumber: this.updateBranchGeneralForm.value.phoneNumber,
      email: this.updateBranchGeneralForm.value.email,
    };

    console.log('Form Data:', general);

    this.branchService.updateBranchGeneral(branchId, general).subscribe({
      next: (updatedBranch) => {
        console.log('Branch general updated:', updatedBranch);
      },
      error: (error) => {
        console.error('Error updating branch:', error);
      },
    });
  }
}
