import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams, HttpRequest } from '@angular/common/http';
import { Observable } from 'rxjs';
import { User } from '../models/user.model';
import { AccountResponse } from '../models/account.model';
import { TransactionResponse } from '../models/transaction.model';
import { CookieService } from 'ngx-cookie-service';
import { environment } from '../../environments/environment';
@Injectable({
  providedIn: 'root',
})
export class AccountsService {
  private baseAccountUrl = `/api/v1/accounts`;
  private baseUserUrl = `/api/v1/users`;
  private baseTransactionUrl = `/api/v1/transactions`;
  private jwt = this.cookieService.get('token');

  private token = `Bearer ${this.jwt}`;

  constructor(private http: HttpClient, private cookieService: CookieService) {}

  fetchUsers(): Observable<any> {
    const headers = new HttpHeaders({
      Authorization: this.token,
    });

    return this.http.get<any>(this.baseUserUrl, { headers });
  }

  createAccount(payload: {
    userId: number;
    accountType: string;
    initialBalance: number;
  }): Observable<any> {
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      Authorization: this.token,
    });
    return this.http.post(this.baseAccountUrl, payload, { headers });
  }

  fetchAccounts(
    filters: { [key: string]: any } = {}
  ): Observable<AccountResponse> {
    const headers = new HttpHeaders({
      Authorization: this.token,
    });

    let params = new HttpParams();
    Object.keys(filters).forEach((key) => {
      if (filters[key] !== null && filters[key] !== undefined) {
        params = params.append(key, filters[key]);
      }
    });

    return this.http.get<AccountResponse>(`${this.baseAccountUrl}/all`, {
      headers,
      params,
    });
  }

  fetchTransactions(filters: { [key: string]: any } = {}): Observable<any> {
    const headers = new HttpHeaders({
      Authorization: this.token,
    });
    let params = new HttpParams();
    Object.keys(filters).forEach((key) => {
      if (filters[key] !== null && filters[key] !== undefined) {
        params = params.append(key, filters[key]);
      }
    });
    return this.http.get<TransactionResponse>(
      `${this.baseTransactionUrl}/all`,
      {
        headers,
        params,
      }
    );
  }

  reverseTransaction(transactionId: number): Observable<any> {
    const headers = new HttpHeaders({
      Authorization: this.token,
    });
    return this.http.post(
      `${this.baseTransactionUrl}/reverse/${transactionId}`,
      {},
      { headers }
    );
  }

  activateAccount(accountId: number): Observable<any> {
    const headers = new HttpHeaders({
      Authorization: this.token,
    });
    return this.http.patch(
      `${this.baseAccountUrl}/activate/by-account/${accountId}`,
      {},
      { headers }
    );
  }
  deactivateAccount(accountId: number): Observable<any> {
    const headers = new HttpHeaders({
      Authorization: this.token,
    });
    return this.http.patch(
      `${this.baseAccountUrl}/deactivate/by-account/${accountId}`,
      {},
      { headers }
    );
  }

  uploadAccount(formData: FormData): Observable<any> {
    const token = this.cookieService.get('token');
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`,
    });

    const url = `${this.baseAccountUrl}/upload/`;
    const req = new HttpRequest('POST', url, formData, {
      headers: headers,
      reportProgress: true,
    });

    return this.http.request(req);
  }
}
