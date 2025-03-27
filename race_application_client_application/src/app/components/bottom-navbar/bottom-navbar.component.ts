import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-bottom-navbar',
  templateUrl: './bottom-navbar.component.html',
  styleUrls: ['./bottom-navbar.component.css']
})
export class BottomNavbarComponent {

  public userEmail = 'mock@email.com';
  public role = 'Administrator';

  constructor(private router: Router) {}

  logout(): void {
    this.router.navigate(['/login']);
  }

}
