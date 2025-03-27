import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Application } from 'src/app/models/application.model';
import { ApplicationService } from 'src/app/services/application.service';

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
  ) {}

  ngOnInit(): void {
    this.applicationService.getAllApplications().subscribe(apps => this.applications = apps);
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
