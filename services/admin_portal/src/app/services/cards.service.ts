import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { User } from '../models/user.model';
import { AccountResponse } from '../models/account.model';
import { TransactionResponse } from '../models/transaction.model';
import { CookieService } from 'ngx-cookie-service';
import { environment } from '../../environments/environment';
@Injectable({
  providedIn: 'root',
})
export class CardsService {
  private baseAccountUrl = `/api/v1/cards`;
  private baseUserUrl = `/api/v1/users`;
  private jwt = this.cookieService.get('token');

  private token = `Bearer ${this.jwt}`;

  constructor(private http: HttpClient, private cookieService: CookieService) {
  }
}
