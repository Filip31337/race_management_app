import { Component, OnInit } from '@angular/core';
import { ApplicationService } from 'src/app/services/application.service';
import { RaceService } from 'src/app/services/race.service';
import { Race } from 'src/app/models/race.model';
import { Application } from 'src/app/models/application.model';
import { ActivatedRoute, Router } from '@angular/router';
import { Location } from '@angular/common';

@Component({
  selector: 'app-application-form',
  templateUrl: './application-form.component.html',
  styleUrls: ['./application-form.component.css']
})
export class ApplicationFormComponent implements OnInit {
  races: Race[] = [];
  application: Partial<Application> = { firstName: '', lastName: '', club: '', raceId: '' };
  isViewMode = false;

  constructor(
    private applicationService: ApplicationService, 
    private raceService: RaceService, 
    public router: Router, 
    private route: ActivatedRoute, 
    private location: Location
  ) {}

  ngOnInit(): void {
    this.raceService.getAllRaces().subscribe(races => {
      this.races = races;
      if (!this.route.snapshot.paramMap.get('id')) {
        const raceIdFromQuery = this.route.snapshot.queryParamMap.get('raceId');
        if (raceIdFromQuery) {
          this.application.raceId = raceIdFromQuery;
        }
      }
    });

    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.isViewMode = true;
      this.applicationService.getApplication(id).subscribe({
        next: appData => this.application = appData,
        error: err => console.error('Error loading application:', err)
      });
    }
  }

  submit(): void {
    if (!this.isViewMode) {
      this.applicationService.createApplication(this.application).subscribe(() => {
        this.router.navigate(['/races']);
      });
    }
  }

  goBack() {
    this.location.back();
  }
}

