import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
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
         { id: '3', name: 'Mock Race 3', distance: 'HalfMarathon' },
         { id: '4', name: 'Mock Race 4', distance: 'MARATHON' },
         { id: '5', name: 'Mock Race 5', distance: '5K' },
         { id: '6', name: 'Mock Race 6', distance: '10K' },
         { id: '7', name: 'Mock Race 7', distance: 'MARATHON' },
         { id: '8', name: 'Mock Race 8', distance: '10K' },
         { id: '9', name: 'Mock Race 9', distance: '5K' },
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
    const params = new HttpParams()
    .set('name', race.name || '')
    .set('distance', race.distance || '');
    return this.http.post<Race>(`${environment.commandApiUrl}/command-service-api/races`, null, { params });
  }

  updateRace(id: string, race: Partial<Race>) {
    const params = new HttpParams()
    .set('name', race.name || '')
    .set('distance', race.distance || '');
    return this.http.put<Race>(`${environment.commandApiUrl}/command-service-api/races/${id}`, null, { params });
  }

  deleteRace(id: string) {
    return this.http.delete(`${environment.commandApiUrl}/command-service-api/races/${id}`);
  }
}