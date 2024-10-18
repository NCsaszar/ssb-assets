import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { CookieService } from 'ngx-cookie-service';
import { Banker } from '../models/banker.model';

@Injectable({
  providedIn: 'root'
})
export class BankerService {

  private bankerUrl = '/api/v1/banker';

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

  getAllBankers(): Observable<Banker[]> {
    return this.httpClient.get<Banker[]>(this.bankerUrl, this.getRequestOptions());
  }

  getBankerById(bankerId: number): Observable<Banker> {
    const url = `${this.bankerUrl}?bankerId=${bankerId}`;
    return this.httpClient.get<Banker>(url, this.getRequestOptions());
  };

  getBankersByBranchId(branchId: number): Observable<Banker[]> {
    const url = `${this.bankerUrl}?branchId=${branchId}`;
    return this.httpClient.get<Banker[]>(url, this.getRequestOptions());
  };

  createBanker(banker: Banker): Observable<Banker> {
    const url = `${this.bankerUrl}`;
    return this.httpClient.post<Banker>(url, banker, this.getRequestOptions());
  }

  deleteBanker(bankerId: number): Observable<void> {
    const url = `${this.bankerUrl}/delete/${bankerId}`;
    return this.httpClient.delete<void>(url, this.getRequestOptions());
  }
}