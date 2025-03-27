import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { race } from 'rxjs';
import { Race } from 'src/app/models/race.model';
import { RaceService } from 'src/app/services/race.service';

@Component({
  selector: 'app-race-list',
  templateUrl: './race-list.component.html',
  styleUrls: ['./race-list.component.css']
})
export class RaceListComponent implements OnInit {
  
  races: Race[] = [];

  constructor(private raceService: RaceService, private router: Router) {}

  ngOnInit(): void {
    this.raceService.getAllRaces().subscribe(races => this.races = races);
  }

  apply(raceId: string): void {
    this.router.navigate(['/application/new'], { queryParams: { raceId } });
  }

  update(raceId: string): void {
    this.router.navigate(['/race/edit', { queryParams: { raceId } }]);
  }

  remove(raceId: string): void {
    console.log("TODO: deleting race with id: " + raceId);
    // todo delete api amn refresh list
  }

  createRace(): void {
    this.router.navigate(['/race/new']);
  }
}
