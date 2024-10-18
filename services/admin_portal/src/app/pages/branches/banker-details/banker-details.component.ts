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

import { BankerService } from '../../../services/banker.service';
import { Banker } from '../../../models/banker.model';
import { AppointmentComponent } from '../appointment/appointment.component';
import { UpdateBankerComponent } from '../../../components/modals/update-banker/update-banker.component';

@Component({
  selector: 'app-banker-details',
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
    AppointmentComponent,
    UpdateBankerComponent,
  ],
  templateUrl: './banker-details.component.html',
  styleUrl: './banker-details.component.css',
})
export class BankerDetailsComponent {
  title = 'Banker Details';

  displayedColumns: string[] = ['code', 'name', 'link'];
  dataSource: MatTableDataSource<Banker> = new MatTableDataSource<Banker>([]);
  bankerId!: number;
  banker: Banker | undefined;

  @ViewChild(MatSort) sort!: MatSort;

  constructor(
    private route: ActivatedRoute,
    private bankerService: BankerService,
    public dialog: MatDialog
  ) {}

  openDialog(bankerId: number): void {
    const dialogRef = this.dialog.open(UpdateBankerComponent, {
      data: { bankerId: bankerId },
    });

    dialogRef.afterClosed().subscribe((result) => {
      console.log(`Dialog result: ${result}`);
    });
  }

  fetchBankerById(bankerId: number): void {
    this.bankerService.getBankerById(bankerId).subscribe({
      next: (banker: any) => {
        this.banker = banker[0] as Banker;
        this.dataSource.data = [this.banker];
        console.log('this.banker:', this.banker);
      },
      error: (error: any) => {
        console.error('Error fetching banker details:', error);
      },
    });
  }

  ngOnInit(): void {
    this.route.params.subscribe((params) => {
      this.bankerId = params['id'];
      this.fetchBankerById(this.bankerId);
    });
  }
}
