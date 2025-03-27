import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { Race } from 'src/app/models/race.model';
import { RaceService } from 'src/app/services/race.service';

@Component({
  selector: 'app-race-form',
  templateUrl: './race-form.component.html',
  styleUrls: ['./race-form.component.css']
})
export class RaceFormComponent {

  race: Partial<Race> = { name: '', distance: '' };

  constructor(private raceService: RaceService, public router: Router) {}
  
  submit(): void {
    this.raceService.createRace(this.race).subscribe(() => {
      this.router.navigate(['/races']);
    });
  }
}
