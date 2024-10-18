import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { BehaviorSubject, Observable, catchError, map, tap, throwError } from 'rxjs';
import { API_CONFIG } from '../utils/config/apiConfig';
import { CookieService } from 'ngx-cookie-service';

@Injectable({
  providedIn: 'root'
})
export class UserUploadService {
  private duplicateStatusSource = new BehaviorSubject<boolean>(false);
  duplicateStatus$ = this.duplicateStatusSource.asObservable();

  constructor(
    private http: HttpClient,
    private cookieService: CookieService
  ) { }

  uploadFile(file: File): Observable<any> {
    const formData = new FormData();
    formData.append('file', file, file.name);

    const headers = this.getHeaders();

    return this.http.post(`${API_CONFIG.uploadUserEndpoint}`, formData, {
      headers,
      responseType: 'text'
    })
      .pipe(
        tap(response => {
          if (response.includes("Duplicates detected")) {
            this.duplicateStatusSource.next(true);
          } else {
            this.duplicateStatusSource.next(false);
          }
        }),
        catchError(error => {
          console.error('Error during file upload:', error);
          return throwError(() => new Error(error.message || "Unknown error occurred during file upload."));
        })
      );
  }



  getPendingDuplicates(): Observable<any[]> {
    const headers = this.getHeaders();
    return this.http.get<any>(`${API_CONFIG.duplicateEndpoint}/pending`, { headers }).pipe( //chain multiple RxJS operators together
      map(response => Object.entries(response).map(([username, status]) => ({ username, status })))
    );
  }

  resolveDuplicates(decisions: any[]): Observable<any> {
    const headers = this.getHeaders();
    const formattedDecisions = decisions.reduce((acc, curr) => { //accumulate decisions
      acc[curr.username] = curr.decision;
      return acc;
    }, {});

    return this.http.post(`${API_CONFIG.duplicateEndpoint}/resolve`, formattedDecisions, { headers, responseType: 'text' })
      .pipe(
        tap(() => this.duplicateStatusSource.next(false)),
        catchError(error => throwError(() => new Error(error.message)))
      );
  }

  downloadLogFile(): Observable<Blob> {
    const headers = this.getHeaders();
    return this.http.get(`${API_CONFIG.logDownloadEndpoint}`, { headers, responseType: 'blob' }).pipe(
      catchError(error => throwError(() => new Error(error.message)))
    );
  }

  private getHeaders(): HttpHeaders {
    const token = this.cookieService.get('token');
    return new HttpHeaders({ 'Authorization': `Bearer ${token}` });
  }
}
