import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Race } from 'src/app/models/race.model';
import { RaceService } from 'src/app/services/race.service';

@Component({
  selector: 'app-race-form',
  templateUrl: './race-form.component.html',
  styleUrls: ['./race-form.component.css']
})
export class RaceFormComponent implements OnInit {

  race: Race = { id: '', name: '', distance: '' };
  isEditMode = false;

  constructor(
    private raceService: RaceService, 
    public router: Router
  ) {
    
  }

  ngOnInit(): void {
    const navigation = this.router.getCurrentNavigation();
    const stateRace = navigation?.extras?.state?.['race'];

    if (stateRace) {
      this.populateForm(stateRace);
      return;
    } else {
      console.warn('No initial race data found in navigation state');
    }
  }

  private populateForm(raceData: Race): void {
    this.race = { ...raceData };
    this.isEditMode = true;
  }
  
  submit(): void {
    if (this.isEditMode) {
      this.raceService.updateRace(this.race.id, this.race).subscribe({
        next: () => this.router.navigate(['/races']),
        error: (err) => console.error('Update failed:', err)
      });
    } else {
      this.raceService.createRace(this.race).subscribe({
        next: () => this.router.navigate(['/races']),
        error: (err) => console.error('Creation failed:', err)
      });
    }
  }
}
