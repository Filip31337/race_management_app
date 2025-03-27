import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from 'src/environments/environment';
import { Race } from '../models/race.model';
import { of } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class RaceService {
  constructor(private http: HttpClient) {}

  getAllRaces() {
    if (!environment.production) {
      const mockRaces: Race[] = [
         { id: '1', name: 'Mock Race 1', distance: '5K' },
         { id: '2', name: 'Mock Race 2', distance: '10K' },
         { id: '3', name: 'Mock Race 3', distance: 'HalfMarathon' }
      ];
      return of(mockRaces);
    }
    return this.http.get<Race[]>(`${environment.queryApiUrl}/query-service-api/races`);
  }

  getRace(id: string) {
    if (!environment.production) {
      const mockRace: Race = { id, name: 'Mock Race '+id, distance: 'Marathon' };
      return of(mockRace);
    }
    return this.http.get<Race>(`${environment.queryApiUrl}/query-service-api/races/${id}`);
  }

  createRace(race: Partial<Race>) {
    return this.http.post<Race>(`${environment.commandApiUrl}/command-service-api/races`, race);
  }

  updateRace(id: string, race: Partial<Race>) {
    return this.http.put<Race>(`${environment.commandApiUrl}/command-service-api/races/${id}`, race);
  }

  deleteRace(id: string) {
    return this.http.delete(`${environment.commandApiUrl}/command-service-api/races/${id}`);
  }
}