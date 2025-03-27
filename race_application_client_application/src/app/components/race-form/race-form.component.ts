import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
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
    public router: Router,
    private route: ActivatedRoute
  ) {
    
  }

  ngOnInit(): void {
    const raceId = this.route.snapshot.paramMap.get('id');
    if (raceId) {
      this.isEditMode = true;
      this.raceService.getRace(raceId).subscribe({
        next: raceData => this.race = raceData,
        error: err => console.error('Error fetching race data:', err)
      });
    }
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
