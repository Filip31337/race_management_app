import { Component, OnDestroy, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { AuthService } from 'src/app/services/auth.service';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent implements OnInit, OnDestroy {

  public isLoggedIn = false;
  private loginSubscription?: Subscription;

  constructor(private router: Router, public authService: AuthService) {}

  ngOnInit(): void {
    this.loginSubscription = this.authService.loggedIn$.subscribe(loggedIn => {
      this.isLoggedIn = loggedIn;
    });
  }

  ngOnDestroy() {
    this.loginSubscription?.unsubscribe();
  }

}
