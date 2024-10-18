import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_CONFIG } from '../../utils/config/apiConfig';

@Injectable({
    providedIn: 'root'
})
export class UserService {

    private apiUrl = `${API_CONFIG.getAllUsersEndpoint}/api/v1/users`; 

    constructor(private http: HttpClient) { }

    getUsers(): Observable<any> {
        return this.http.get<any>(`${this.apiUrl}`);
      }
      
}
