import { Component, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, RouterLink, RouterOutlet } from '@angular/router';  // RouterLink, RouterOutlet - you don't import Angular directives in the 'imports'array of the decorator.  Instead, use directly in the template...?

import { MatSort, MatSortModule } from '@angular/material/sort';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';

import { AppointmentService } from '../../../services/appointment.service';
import { Appointment } from '../../../models/appointment.model';

@Component({
  selector: 'app-appointment-details',
  standalone: true,
  imports: [
    CommonModule,
    RouterLink,
    RouterOutlet,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatCardModule,
    MatDialogModule,
  ],
  templateUrl: './appointment-details.component.html',
  styleUrl: './appointment-details.component.css'
})
export class AppointmentDetailsComponent {
  title = 'Appointment Details';

  appointmentId!: number;
  appointment: Appointment | undefined;

  constructor(
    private route: ActivatedRoute,
    private appointmentService: AppointmentService,
    public dialog: MatDialog
  ) {}

  // openDialog(appointmentId: number): void {
  //   const dialogRef = this.dialog.open(UpdateAppointmentComponent, {
  //     data: { appointmentId: appointmentId },
  //   });

  //   dialogRef.afterClosed().subscribe((result) => {
  //     console.log(`Dialog result: ${result}`);
  //   });
  // }

  fetchAppointmentById(appointmentId: number): void {
    this.appointmentService.getAppointmentById(appointmentId).subscribe({
      next: (appointment: any) => {
        this.appointment = appointment[0] as Appointment;
        // this.dataSource.data = [this.appointment];
        console.log('this.appointment:', this.appointment);
      },
      error: (error: any) => {
        console.error('Error fetching appointment details:', error);
      },
    });
  }

  ngOnInit(): void {
    this.route.params.subscribe((params) => {
      this.appointmentId = params['id'];
      this.fetchAppointmentById(this.appointmentId);
    });
  }
}
