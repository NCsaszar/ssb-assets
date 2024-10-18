import { Component, OnInit, ViewChild } from '@angular/core';
import { AccountsService } from '../../../services/accounts.service';
import { Account, AccountResponse } from '../../../models/account.model';
import { MatTableDataSource } from '@angular/material/table';
import { MatTableModule } from '@angular/material/table';
import { DatePipe, CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatPaginator, MatPaginatorModule } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatSortModule } from '@angular/material/sort';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatDividerModule } from '@angular/material/divider';
import { MatIconModule } from '@angular/material/icon';
import { MatSelectModule } from '@angular/material/select';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { provideNativeDateAdapter } from '@angular/material/core';
import { FormControl, ReactiveFormsModule, FormGroup } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MatSlideToggleModule } from '@angular/material/slide-toggle';

@Component({
  selector: 'app-accounts-view',
  standalone: true,
  imports: [
    MatTableModule,
    CommonModule,
    MatCardModule,
    MatButtonModule,
    MatPaginatorModule,
    MatSortModule,
    MatFormFieldModule,
    MatInputModule,
    MatDividerModule,
    MatIconModule,
    MatSelectModule,
    MatDatepickerModule,
    ReactiveFormsModule,
    MatSlideToggleModule,
  ],
  templateUrl: './accounts-view.component.html',
  styleUrl: './accounts-view.component.css',
  providers: [DatePipe, provideNativeDateAdapter()],
})
export class AccountsViewComponent implements OnInit {
  dataSource = new MatTableDataSource<Account>();
  accountTypes = ['All', 'Checking', 'Savings', 'Credit', 'Loan'];
  displayedColumns: string[] = [
    'accountNumber',
    'userId',
    'accountType',
    'balance',
    'createdAt',
    'isActive',
    'actions',
  ];
  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;

  startEndRange = new FormGroup({
    start: new FormControl(),
    end: new FormControl(),
    minBalance: new FormControl(),
    maxBalance: new FormControl(),
    isActive: new FormControl(),
    accountType: new FormControl('ALL'),
  });
  constructor(
    private accountsService: AccountsService,
    private datePipe: DatePipe,
    private _snackBar: MatSnackBar
  ) {}

  applyFilter(event: Event) {
    const value = (event.target as HTMLInputElement).value;
    this.dataSource.filter = value.trim().toLowerCase();
  }

  ngOnInit(): void {
    this.startEndRange.valueChanges.subscribe((range) => {
      if (range) {
        this.loadAccounts();
      }
    });
  }

  loadAccounts(): void {
    console.log(this.startEndRange.get('accountType')?.value);
    let queryParams: any = {
      page: this.paginator.pageIndex,
      size: this.paginator.pageSize,
      sortBy:
        this.sort.active && this.sort.direction
          ? `${this.sort.active},${this.sort.direction}`
          : 'createdAt,desc',
      accountType:
        this.startEndRange.get('accountType')?.value?.toUpperCase() === 'ALL'
          ? ''
          : this.startEndRange.get('accountType')?.value?.toUpperCase(),
      startDate: this.startEndRange.value.start
        ? this.startEndRange.value.start.toISOString()
        : '',
      endDate: this.startEndRange.value.end
        ? this.startEndRange.value.end.toISOString()
        : '',
      minBalance: this.startEndRange.get('minBalance')?.value,
      maxBalance: this.startEndRange.get('maxBalance')?.value,
    };
    if (this.startEndRange.get('isActive')?.value === true) {
      queryParams.isActive = true;
    }

    this.accountsService.fetchAccounts(queryParams).subscribe({
      next: (response) => {
        console.log(response);
        this.dataSource.data = response.data.content;

        this.updatePaginator(
          response.data.totalElements,
          response.data.pageable.pageSize,
          response.data.pageable.pageNumber
        );
        console.log(this.dataSource);
      },
      error: (error) => {
        console.error('Error fetching accounts: ', error);
      },
    });
  }

  updatePaginator(totalItems: number, pageSize: number, pageIndex: number) {
    this.paginator.length = totalItems;
    this.paginator.pageSize = pageSize;
    this.paginator.pageIndex = pageIndex;
  }

  ngAfterViewInit() {
    this.loadAccounts();
    this.paginator.page.subscribe((event) => {
      this.loadAccounts();
    });
    this.sort.sortChange.subscribe(() => {
      this.paginator.pageIndex = 0;
      this.loadAccounts();
    });
  }

  activateAccount(accountId: number): void {
    this.accountsService.activateAccount(accountId).subscribe({
      next: () => {
        console.log('Account activated successfully');
        const accountIndex = this.dataSource.data.findIndex(
          (account) => account.accountId === accountId
        );
        if (accountIndex !== -1) {
          this.dataSource.data[accountIndex].isActive = true;
          this.refreshTable();
          this.openSnackBar(
            `Account ${this.dataSource.data[accountIndex].accountNumber} activated`,
            'Dismiss'
          );
        }
      },
      error: (error) => {
        console.error('Error activating account: ', error);
      },
    });
  }

  deactivateAccount(accountId: number): void {
    this.accountsService.deactivateAccount(accountId).subscribe({
      next: () => {
        console.log('Account deactivated successfully');
        const accountIndex = this.dataSource.data.findIndex(
          (account) => account.accountId === accountId
        );
        if (accountIndex !== -1) {
          this.dataSource.data[accountIndex].isActive = false;
          this.refreshTable();
          this.openSnackBar(
            `Account ${this.dataSource.data[accountIndex].accountNumber} deactivated`,
            'Dismiss'
          );
        }
      },
      error: (error) => {
        console.error('Error deactivating account: ', error);
      },
    });
  }

  openSnackBar(message: string, action: string) {
    this._snackBar.open(message, action, {
      duration: 5000,
    });
  }

  refreshTable(): void {
    // Reassign the array reference to tell angular that the data changed instead
    // of calling api again
    this.dataSource.data = [...this.dataSource.data];
  }

  clearFilters(): void {
    this.startEndRange.reset({
      start: null,
      end: null,
      minBalance: null,
      maxBalance: null,
      isActive: '',
    });
    this.loadAccounts();
  }
}
