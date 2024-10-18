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

import { AppointmentService } from '../../../services/appointment.service';
import { Appointment } from '../../../models/appointment.model';
@Component({
  selector: 'app-appointment',
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
  ],
  templateUrl: './appointment.component.html',
  styleUrl: './appointment.component.css'
})
export class AppointmentComponent implements OnChanges {
  title = 'Appointments';

  @Input() bankerId!: number;
  displayedColumns: string[] = ['customer', 'time', 'link', 'delete'];
  dataSource: MatTableDataSource<Appointment> = new MatTableDataSource<Appointment>([]);
  appointments: Appointment[] = [];
  appointmentsByBanker: Appointment[] = [];

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;

  constructor(private appointmentService: AppointmentService, public dialog: MatDialog) {
    // this.dataSource = new MatTableDataSource(this.bankers);
  }

  ngOnChanges(changes: SimpleChanges): void {
    if ('bankerId' in changes && changes['bankerId'].currentValue) {
      const bankerId = changes['bankerId'].currentValue;
      this.fetchAppointmentsByBankerId(bankerId);
    }
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

  fetchAppointmentsByBankerId(bankerId: number): void {
    this.appointmentService.getAppointmentsByBankerId(bankerId).subscribe({
      next: (appointments: Appointment[]) => {
        this.appointments = appointments;
        this.dataSource.data = this.appointments;
        console.log('this.appointments:', this.appointments);
      },
      error: (error: any) => {
        console.error('Error fetching appointments details:', error);
      },
    });
  }

  deleteAppointment(appointmentId: number): void {
    if (confirm('Are you sure you want to delete this appointment?')) {
      this.appointmentService.deleteAppointment(appointmentId).subscribe({
        next: () => {
          this.appointments = this.appointments.filter(
            (appointment) => appointment.appointmentId !== appointmentId
          );
          this.dataSource.data = this.appointments;
          // Optionally display a success message or perform any other action
        },
        error: (error: any) => {
          console.error('Error deleting appointment: ', error);
          // Optionally display an error message or perform any other action
        },
      });
    }
  }

}
