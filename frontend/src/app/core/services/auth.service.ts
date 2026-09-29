import { Injectable, signal, computed } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { Observable, tap } from 'rxjs';
import { environment } from '../../../environments/environment';

interface AuthResponse {
  token: string;
  username: string;
}

interface LoginRequest {
  username: string;
  password: string;
}

interface RegisterRequest {
  username: string;
  email: string;
  password: string;
}

@Injectable({ providedIn: 'root' })
export class AuthService {
  private url = `${environment.apiUrl}/auth`;
  token = signal<string | null>(localStorage.getItem('token'));
  username = signal<string | null>(localStorage.getItem('username'));
  isAuthenticated = computed(() => !!this.token());

  constructor(private http: HttpClient, private router: Router) {}

  login(data: LoginRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.url}/login`, data).pipe(
      tap(res => this.setSession(res))
    );
  }

  register(data: RegisterRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.url}/register`, data).pipe(
      tap(res => this.setSession(res))
    );
  }

  logout(): void {
    this.token.set(null);
    this.username.set(null);
    localStorage.removeItem('token');
    localStorage.removeItem('username');
    this.router.navigate(['/login']);
  }

  private setSession(res: AuthResponse): void {
    this.token.set(res.token);
    this.username.set(res.username);
    localStorage.setItem('token', res.token);
    localStorage.setItem('username', res.username);
  }
}
