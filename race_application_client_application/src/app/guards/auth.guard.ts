import { Injectable } from '@angular/core';
import { CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, Router } from '@angular/router';
import { AuthService } from '../services/auth.service';

@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate {
  constructor(private authService: AuthService, private router: Router) {}

  canActivate(next: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean {
    if (!this.authService.isLoggedIn()) {
      this.router.navigate(['/login']);
      return false;
    }
    
    const requiredRole = next.data['requiredRole'];
    if (requiredRole) {
      const userRole = this.authService.getRole();
      if (userRole !== requiredRole) {
        this.clearAuthContext();
        this.router.navigate(['/login']);
        return false;
      }
    }
    return true;
  }

  private clearAuthContext(): void {
    this.authService.logout();
  }
}
