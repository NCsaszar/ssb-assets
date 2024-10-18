import { Component, OnInit, ViewChild } from '@angular/core';
import { AccountsService } from '../../../services/accounts.service';
import { Transaction } from '../../../models/transaction.model';
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
import { MatDialogModule } from '@angular/material/dialog';
import { MatDialog } from '@angular/material/dialog';
import { ConfirmReversalModalComponent } from '../../../components/modals/confirm-reversal-modal/confirm-reversal-modal.component';
import { distinctUntilChanged, map } from 'rxjs/operators';

@Component({
  selector: 'app-transactions-view',
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
    MatDialogModule,
  ],
  templateUrl: './transactions-view.component.html',
  styleUrl: './transactions-view.component.css',
  providers: [DatePipe, provideNativeDateAdapter()],
})
export class TransactionsViewComponent implements OnInit {
  constructor(
    private accountsService: AccountsService,
    private dialog: MatDialog,
    private _snackBar: MatSnackBar
  ) {}
  dataSource = new MatTableDataSource<Transaction>();
  transactionTypes = [
    'Deposit',
    'Withdrawal',
    'Payment',
    'Transfer',
    'Reversal',
  ];
  displayedColumns: string[] = [
    'transactionId',
    'accountNumber',
    'transactionType',
    'amount',
    'dateTime',
    'description',
    'actions',
  ];
  filters = new FormGroup({
    start: new FormControl(),
    end: new FormControl(),
    minAmount: new FormControl(),
    maxAmount: new FormControl(),
    transactionType: new FormControl(),
    lastFour: new FormControl(),
  });
  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;

  performSearch(): void {
    this.loadTransactions(this.filters.value.lastFour);
  }

  ngOnInit(): void {
    this.filters.valueChanges
      .pipe(
        map((values) => {
          const { lastFour, ...rest } = values;
          return rest;
        }),
        distinctUntilChanged((a, b) => JSON.stringify(a) === JSON.stringify(b))
      )
      .subscribe(() => {
        this.loadTransactions();
      });
  }

  updatePaginator(totalItems: number, pageSize: number, pageIndex: number) {
    this.paginator.length = totalItems;
    this.paginator.pageSize = pageSize;
    this.paginator.pageIndex = pageIndex;
  }

  ngAfterViewInit() {
    this.loadTransactions();
    this.paginator.page.subscribe((event) => {
      this.loadTransactions();
    });
    this.sort.sortChange.subscribe(() => {
      this.paginator.pageIndex = 0;
      this.loadTransactions();
    });
  }
  clearFilters(): void {
    this.filters.reset({
      start: null,
      end: null,
      minAmount: null,
      maxAmount: null,
      transactionType: null,
    });
    this.loadTransactions();
  }
  reverseTransaction(transaction: Transaction): void {
    const dialogRef = this.dialog.open(ConfirmReversalModalComponent, {
      width: '250px',
      data: { transactionId: transaction.transactionId },
    });

    dialogRef.afterClosed().subscribe((result) => {
      if (result) {
        this.accountsService
          .reverseTransaction(transaction.transactionId)
          .subscribe({
            next: () => {
              this.openSnackBar(
                `Transaction ${transaction.transactionId} for amount of $${transaction.amount} reversed`,
                'Dismiss'
              );
            },
            error: (error) => {
              this.openSnackBar(
                `Error reversing transaction ${transaction.transactionId}`,
                'Dismiss'
              );
            },
          });
      }
    });
  }

  openSnackBar(message: string, action: string) {
    this._snackBar.open(message, action, {
      duration: 5000,
    });
  }

  loadTransactions(lastFour?: string): void {
    let queryParams: any = {
      page: this.paginator.pageIndex,
      size: this.paginator.pageSize,
      sortBy:
        this.sort.active && this.sort.direction
          ? `${this.sort.active},${this.sort.direction}`
          : 'dateTime,desc',
      transactionTypeStr: this.filters.get('transactionType')?.value
        ? this.filters.get('transactionType')?.value
        : null,
      startDate: this.filters.value.start
        ? this.filters.value.start.toISOString()
        : '',
      endDate: this.filters.value.end
        ? this.filters.value.end.toISOString()
        : '',
      minAmount: this.filters.get('minAmount')?.value,
      maxAmount: this.filters.get('maxAmount')?.value,
      lastFour: lastFour,
    };
    this.accountsService.fetchTransactions(queryParams).subscribe({
      next: (response) => {
        this.dataSource.data = response.data.content;

        this.updatePaginator(
          response.data.totalElements,
          response.data.pageable.pageSize,
          response.data.pageable.pageNumber
        );
      },
      error: (error) => {
        console.error('Error fetching accounts: ', error);
      },
    });
  }
}
