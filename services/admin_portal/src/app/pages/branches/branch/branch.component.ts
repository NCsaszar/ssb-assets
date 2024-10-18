import { AfterViewInit, OnInit, Component, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink, RouterOutlet } from '@angular/router';

import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { MatPaginator, MatPaginatorModule } from '@angular/material/paginator';
import { MatSort, MatSortModule } from '@angular/material/sort';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';

import { Branch } from '../../../models/branch.model';
import { BranchService } from '../../../services/branch.service';
import { CreateBranchComponent } from '../../../components/modals/create-branch/create-branch.component';
import { BranchCreationUploadComponent } from '../../../components/modals/branch-creation-upload/branch-creation-upload.component';

@Component({
  selector: 'app-branches',
  standalone: true,
  imports: [
    CommonModule,
    RouterLink,
    RouterOutlet,
    MatFormFieldModule,
    MatInputModule,
    MatTableModule,
    MatSortModule,
    MatPaginatorModule,
    MatButtonModule,
    MatIconModule,
    MatDialogModule,
    CreateBranchComponent,
    BranchCreationUploadComponent,
  ],
  templateUrl: './branch.component.html',
  styleUrl: './branch.component.css',
})
export class BranchComponent implements OnInit, AfterViewInit {
  title = 'Secure Sentinel Bank Branches';

  displayedColumns: string[] = ['code', 'name', 'link', 'delete'];
  dataSource: MatTableDataSource<Branch>;
  branches: Branch[] = [];
  filteredBranchList: Branch[] = [];

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;

  constructor(private branchService: BranchService, public dialog: MatDialog) {
    this.dataSource = new MatTableDataSource(this.branches);
  }

  openDialog() {
    const dialogRef = this.dialog.open(CreateBranchComponent);

    dialogRef.afterClosed().subscribe((result) => {
      console.log(`Dialog result: ${result}`);
    });
  }

  ngAfterViewInit() {
    this.dataSource.paginator = this.paginator;
    this.dataSource.sort = this.sort;
  }

  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();

    if (this.dataSource.paginator) {
      this.dataSource.paginator.firstPage();
    }
  }

  fetchAllBranches(): void {
    this.branchService.getAllBranches().subscribe({
      next: (response) => {
        console.log('response: ', response);
        this.branches = response;
        this.dataSource.data = response;
      },
      error: (error) => {
        console.error('Error fetching branches: ', error);
      },
    });
  }

  deleteBranch(branchId: number): void {
    if (confirm('Are you sure you want to delete this branch?')) {
      this.branchService.deleteBranch(branchId).subscribe({
        next: () => {
          this.branches = this.branches.filter(
            (branch) => branch.branchId !== branchId
          );
          this.dataSource.data = this.branches;
        },
        error: (error: any) => {
          console.error('Error deleting branch: ', error);
        },
      });
    }
  }

  ngOnInit(): void {
    this.fetchAllBranches();
  }
}
