import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Application } from 'src/app/models/application.model';
import { ApplicationService } from 'src/app/services/application.service';
import { AuthService } from 'src/app/services/auth.service';

@Component({
  selector: 'app-application-list',
  templateUrl: './application-list.component.html',
  styleUrls: ['./application-list.component.css']
})
export class ApplicationListComponent implements OnInit {
  applications: Application[] = [];

  constructor(
    private applicationService: ApplicationService,
    private router: Router,
    public authService: AuthService
  ) {}

  ngOnInit(): void {
    const role = this.authService.getRole(); 
    if (role === 'ROLE_ADMINISTRATOR') {
      console.log("(ApplicationListComponent) ngOnInit, entered admin condition! role: " + role);
      this.applicationService.getAllApplications().subscribe(apps => this.applications = apps);
    } else {
      console.log("(ApplicationListComponent) ngOnInit, entered non-admin condition! role: " + role);
      this.applicationService.getMyApplications().subscribe(apps => this.applications = apps);
    }
  }

  deleteApplication(id: string): void {
    this.applicationService.deleteApplication(id).subscribe(() => {
      this.applications = this.applications.filter(app => app.id !== id);
    });
  }

  viewApplication(id: string): void {
    this.router.navigate(['/application/details', id]);
  }
}
