import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable, of } from 'rxjs';
import { environment } from 'src/environments/environment';
import { jwtDecode } from 'jwt-decode';

export interface AuthRequest {
  email: string;
  password: string;
}

export interface AuthResponse {
  token: string;
}

interface DecodedToken {
  sub: string;
  role?: string;
  exp?: number;
}

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  constructor(private http: HttpClient) {
    this.checkInitialToken();
  }

  private tokenKey = 'initialTokenValue';
  
  private loggedInSubject = new BehaviorSubject<boolean>(false);
  loggedIn$ = this.loggedInSubject.asObservable();

  login(authRequest: AuthRequest): Observable<AuthResponse> {
    if (!environment.production) {
      if (authRequest.email.startsWith("admin")) {
        return of({ token: environment.mockTokens.admin });
      } else {
        return of({ token: environment.mockTokens.applicant });
      }
    }
    return this.http.post<AuthResponse>(`${environment.commandApiUrl}/auth/login`, authRequest);
  }

  logout() {
    localStorage.removeItem(this.tokenKey);
    this.updateLoggedInState();
  }

  storeToken(token: string) {
    localStorage.setItem(this.tokenKey, token);
    this.updateLoggedInState();
  }

  getToken(): string | null {
    return localStorage.getItem(this.tokenKey);
  }

  getDecodedToken(): DecodedToken | null {
    const token = this.getToken();
    if (!token) return null;
    try {
      return jwtDecode<DecodedToken>(token);
    } catch {
      return null;
    }
  }

  getRole(): string | null {
    const decoded = this.getDecodedToken();
    return decoded?.role || null;
  }

  isLoggedIn(): boolean {
    const token = this.getToken();
    console.log("isLoggedIn with token: " + token);
    if (!token) return false;
    const decoded = this.getDecodedToken();
    console.log("isLoggedIn with decoded token: " + JSON.stringify(decoded));
    if (!decoded?.exp) return true; 
    if (!environment.production) {
      // skip expiration check on mocked tokens for dev
      return true;
    }
    const now = Math.floor(Date.now() / 1000);
    return now < decoded.exp;
  }

  private updateLoggedInState() {
    const loggedIn = this.isLoggedIn();
    console.log("updateLoggedInState(), upfating state with loggedIn: " + loggedIn);
    this.loggedInSubject.next(loggedIn);
  }

  private checkInitialToken() {
    const exists = !!this.getToken();
    this.loggedInSubject.next(exists && this.isLoggedIn());
  }
}

