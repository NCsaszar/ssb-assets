import {
  Component,
  ViewChild,
  Input,
  OnChanges,
  SimpleChanges,
  OnInit,
} from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, RouterLink, RouterOutlet } from '@angular/router';

import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { MatPaginator, MatPaginatorModule } from '@angular/material/paginator';
import { MatSort, MatSortModule } from '@angular/material/sort';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';

import { BankerService } from '../../../services/banker.service';
import { Banker } from '../../../models/banker.model';
import { CreateBankerComponent } from '../../../components/modals/create-banker/create-banker.component';

@Component({
  selector: 'app-banker',
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
    CreateBankerComponent,
  ],
  templateUrl: './banker.component.html',
  styleUrl: './banker.component.css',
})
export class BankerComponent implements OnChanges {
  title = 'Bankers';

  @Input() branchId!: number;
  displayedColumns: string[] = ['code', 'name', 'link', 'delete'];
  dataSource: MatTableDataSource<Banker> = new MatTableDataSource<Banker>([]);
  bankers: Banker[] = [];
  bankersByBranch: Banker[] = [];

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;

  constructor(private bankerService: BankerService, public dialog: MatDialog) {
    // this.dataSource = new MatTableDataSource(this.bankers);
  }

  ngOnChanges(changes: SimpleChanges): void {
    if ('branchId' in changes && changes['branchId'].currentValue) {
      const branchId = changes['branchId'].currentValue;
      this.fetchBankersByBranchId(branchId);
    }
  }

  openDialog() {
    const dialogRef = this.dialog.open(CreateBankerComponent);

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

  fetchBankersByBranchId(branchId: number): void {
    this.bankerService.getBankersByBranchId(branchId).subscribe({
      next: (bankers: Banker[]) => {
        this.bankers = bankers;
        this.dataSource.data = this.bankers;
        console.log('this.bankers:', this.bankers);
      },
      error: (error: any) => {
        console.error('Error fetching bankers details:', error);
      },
    });
  }

  deleteBanker(bankerId: number): void {
    if (confirm('Are you sure you want to delete this banker?')) {
      this.bankerService.deleteBanker(bankerId).subscribe({
        next: () => {
          this.bankers = this.bankers.filter(
            (banker) => banker.bankerId !== bankerId
          );
          this.dataSource.data = this.bankers;
          // Optionally display a success message or perform any other action
        },
        error: (error: any) => {
          console.error('Error deleting banker: ', error);
          // Optionally display an error message or perform any other action
        },
      });
    }
  }
}
