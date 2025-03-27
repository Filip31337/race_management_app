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

  constructor(
    private applicationService: ApplicationService, 
    private raceService: RaceService, 
    public router: Router, 
    private route: ActivatedRoute, 
    private location: Location) {}

  ngOnInit(): void {
    this.raceService.getAllRaces().subscribe(races => {
      this.races = races;
      const raceIdFromQuery = this.route.snapshot.queryParamMap.get('raceId');
      if (raceIdFromQuery) {
        this.application.raceId = raceIdFromQuery;
      }
    });
  }

  submit(): void {
    this.applicationService.createApplication(this.application).subscribe(() => {
      this.router.navigate(['/applications']);
    });
  }

  goBack() {
    this.location.back();
  }
}

