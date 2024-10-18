import { Component, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, RouterLink, RouterOutlet } from '@angular/router';

import { MatSort, MatSortModule } from '@angular/material/sort';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';

import { BranchService } from '../../../services/branch.service';
import { Branch } from '../../../models/branch.model';
import { BankerComponent } from '../banker/banker.component';
import { UpdateBranchComponent } from '../../../components/modals/update-branch/update-branch.component';
import { UpdateBranchLocationComponent } from '../../../components/modals/update-branch-location/update-branch-location.component';
import { UpdateBranchGeneralComponent } from '../../../components/modals/update-branch-general/update-branch-general.component';

@Component({
  selector: 'app-branch-details',
  standalone: true,
  imports: [
    CommonModule,
    RouterLink,
    RouterOutlet,
    MatFormFieldModule,
    MatInputModule,
    MatTableModule,
    MatSortModule,
    MatButtonModule,
    MatCardModule,
    MatDialogModule,
    BankerComponent,
    UpdateBranchComponent,
    UpdateBranchLocationComponent,
  ],
  templateUrl: './branch-details.component.html',
  styleUrl: './branch-details.component.css',
})
export class BranchDetailsComponent {
  title = 'Branch Details';

  displayedColumns: string[] = ['code', 'name', 'link'];
  dataSource: MatTableDataSource<Branch> = new MatTableDataSource<Branch>([]);
  branchId!: number;
  branch: Branch | undefined;

  @ViewChild(MatSort) sort!: MatSort;

  constructor(
    private route: ActivatedRoute,
    private branchService: BranchService,
    public dialog: MatDialog
  ) {}

  openDialog(branchId: number): void {
    const dialogRef = this.dialog.open(UpdateBranchComponent, {
      data: { branchId: branchId },
    });

    dialogRef.afterClosed().subscribe((result) => {
      console.log(`Dialog result: ${result}`);
    });
  }

  openLocationDialog(branchId: number): void {
    const dialogRef = this.dialog.open(UpdateBranchLocationComponent, {
      data: { branchId: branchId },
    });

    dialogRef.afterClosed().subscribe((result) => {
      console.log(`Dialog result: ${result}`);
    });
  }

  openGeneralDialog(branchId: number): void {
    const dialogRef = this.dialog.open(UpdateBranchGeneralComponent, {
      data: { branchId: branchId },
    });

    dialogRef.afterClosed().subscribe((result) => {
      console.log(`Dialog result: ${result}`);
    });
  }

  fetchBranchById(branchId: number): void {
    this.branchService.getBranchById(branchId).subscribe({
      next: (branch: any) => {
        this.branch = branch[0] as Branch;
        this.dataSource.data = [this.branch];
        console.log('this.branch:', this.branch);
      },
      error: (error: any) => {
        console.error('Error fetching branch details:', error);
      },
    });
  }

  ngOnInit(): void {
    this.route.params.subscribe((params) => {
      this.branchId = params['id'];
      this.fetchBranchById(this.branchId);
    });
  }
}
