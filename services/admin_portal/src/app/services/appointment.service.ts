import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { CookieService } from 'ngx-cookie-service';
import { Appointment } from '../models/appointment.model';

@Injectable({
  providedIn: 'root'
})
export class AppointmentService {

  private appointmentUrl = '/api/v1/appointment';

  constructor(private httpClient: HttpClient, private cookieService: CookieService) { }

  private getHeaders(): HttpHeaders {
    const token = this.cookieService.get('token');
    return new HttpHeaders({
      Authorization: `Bearer ${token}`,
    });
  }

  private getRequestOptions(): { headers: HttpHeaders } {
    return { headers: this.getHeaders() };
  }

  getAllBankers(): Observable<Appointment[]> {
    return this.httpClient.get<Appointment[]>(this.appointmentUrl, this.getRequestOptions());
  }

  getAppointmentById(appointmentId: number): Observable<Appointment> {
    const url = `${this.appointmentUrl}?appointmentId=${appointmentId}`;
    return this.httpClient.get<Appointment>(url, this.getRequestOptions());
  }

  getAppointmentsByBankerId(bankerId: number): Observable<Appointment[]> {
    const url = `${this.appointmentUrl}?bankerId=${bankerId}`;
    return this.httpClient.get<Appointment[]>(url, this.getRequestOptions());
  };

  createAppointment(appointment: Appointment): Observable<Appointment> {
    const url = `${this.appointmentUrl}`;
    return this.httpClient.post<Appointment>(url, appointment, this.getRequestOptions());
  }

  deleteAppointment(appointmentId: number): Observable<void> {
    const url = `${this.appointmentUrl}/delete/${appointmentId}`;
    return this.httpClient.delete<void>(url, this.getRequestOptions());
  }
}