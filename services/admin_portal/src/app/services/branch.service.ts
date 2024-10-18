import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpEvent, HttpRequest } from '@angular/common/http';
import { Observable } from 'rxjs';
import { CookieService } from 'ngx-cookie-service';
import { Branch } from '../models/branch.model';
import { BranchLocation } from '../models/branch-location.model';
import { BranchGeneral } from '../models/branch-general.model';

@Injectable({
  providedIn: 'root'
})
export class BranchService {

  private branchUrl = '/api/v1/branch';

  constructor(private httpClient: HttpClient, private cookieService: CookieService) { }

  private getHeaders(): HttpHeaders {
    const token = this.cookieService.get('token');
    return new HttpHeaders({
      'Authorization': `Bearer ${token}`,
    });
  }

  private getRequestOptions(): { headers: HttpHeaders } {
    return { headers: this.getHeaders() };
  }

  getAllBranches(): Observable<Branch[]> {
    return this.httpClient.get<Branch[]>(this.branchUrl, this.getRequestOptions());
  }

  getBranchById(branchId: number): Observable<Branch> {
    const url = `${this.branchUrl}?branchId=${branchId}`;
    return this.httpClient.get<Branch>(url, this.getRequestOptions());
  };

  createBranch(branch: Branch): Observable<Branch> {
    return this.httpClient.post<Branch>(this.branchUrl, branch, this.getRequestOptions());
  }

  updateBranch(branch: Branch): Observable<Branch> {  // this is missing branchId?  Endpoint looks off.
    const url = `${this.branchUrl}/update/`;
    return this.httpClient.put<Branch>(url, branch, this.getRequestOptions());
  }

  updateBranchLocation(branchId: number, location: Partial<BranchLocation>): Observable<Branch> {
    const url = `${this.branchUrl}/update/${branchId}`;
    return this.httpClient.put<Branch>(url, location, this.getRequestOptions());
  }

  updateBranchGeneral(branchId: number, general: Partial<BranchGeneral>): Observable<Branch> {
    const url = `${this.branchUrl}/update/${branchId}`;
    return this.httpClient.put<Branch>(url, general, this.getRequestOptions());
  }

  deleteBranch(branchId: number): Observable<void> {
    const url = `${this.branchUrl}/delete/${branchId}`;
    return this.httpClient.delete<void>(url, this.getRequestOptions());
  }

  uploadBranch(file: FormData): Observable<any> {
    const url = `${this.branchUrl}/upload`;
    const req = new HttpRequest('POST', url, file, {
      headers: this.getHeaders(),
      reportProgress: true,
    });

    return this.httpClient.request(req);
  }

  downloadReport(reportType: string): Observable<Blob> {
    const url = `${this.branchUrl}/download-report?type=${reportType}`;
    return this.httpClient.get(url, { responseType: 'blob' });
  }
}