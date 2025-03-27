import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Race } from 'src/app/models/race.model';
import { AuthService } from 'src/app/services/auth.service';
import { RaceService } from 'src/app/services/race.service';

@Component({
  selector: 'app-race-list',
  templateUrl: './race-list.component.html',
  styleUrls: ['./race-list.component.css']
})
export class RaceListComponent implements OnInit {
  
  races: Race[] = [];

  constructor(private raceService: RaceService, private router: Router, public authService: AuthService) {}

  ngOnInit(): void {
    this.loadRaces();
  }

  private loadRaces(): void {
    this.raceService.getAllRaces().subscribe(races => this.races = races);
  }

  apply(raceId: string): void {
    this.router.navigate(['/application/new'], { queryParams: { raceId } });
  }

  update(race: Race): void {
    this.router.navigate(['/race/edit', race.id]);
  }

  remove(raceId: string): void {
    this.raceService.deleteRace(raceId).subscribe(() => {
      this.loadRaces();
    });
  }

  createRace(): void {
    this.router.navigate(['/race/new']);
  }
}
