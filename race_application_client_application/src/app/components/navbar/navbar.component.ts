import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent {

  userEmail = 'mock@email.com';
  role = 'Administrator';

  constructor(private router: Router) {}

  logout(): void {
    this.router.navigate(['/login']);
  }

}
