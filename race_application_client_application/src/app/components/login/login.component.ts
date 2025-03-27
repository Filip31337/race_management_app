import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService, AuthRequest, AuthResponse } from 'src/app/services/auth.service';


@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  email = '';
  password = '';
  errorMessage = '';

  constructor(private authService: AuthService, private router: Router) {}

  login(): void {
    const request: AuthRequest = { email: this.email, password: this.password };
      this.authService.login(request).subscribe({
        next: (response: AuthResponse) => {
          this.authService.storeToken(response.token);
          this.router.navigate(['/races']);
        },
        error: () => {
          this.errorMessage = 'Invalid credentials';
        }
      });
  }
}
