import { Component, OnDestroy, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { AuthService } from 'src/app/services/auth.service';

@Component({
  selector: 'app-bottom-navbar',
  templateUrl: './bottom-navbar.component.html',
  styleUrls: ['./bottom-navbar.component.css']
})
export class BottomNavbarComponent implements OnInit, OnDestroy {

  public userEmail = 'Unknown';
  public role = 'Unknown';
  isLoggedIn = false;

  private loginSubscription?: Subscription;

  constructor(private router: Router, public authService: AuthService) {}

  ngOnInit(): void {
    this.loginSubscription = this.authService.loggedIn$.subscribe(loggedIn => {
      console.log("(BottomNavbarComponent) change in loginSubscription, loggedIn: " + loggedIn);
      this.isLoggedIn = loggedIn;
      if (loggedIn) {
        const decoded = this.authService.getDecodedToken();
        this.userEmail = decoded?.sub || 'Unknown';
        this.role = decoded?.role || 'Unknown';
      } else {
        this.userEmail = 'Unknown';
        this.role = 'Unknown';
      }
    });
  }

  logout(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }

  ngOnDestroy() {
    this.loginSubscription?.unsubscribe();
  }

}
