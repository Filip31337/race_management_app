import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from 'src/environments/environment';
import { Race } from '../models/race.model';

@Injectable({
  providedIn: 'root'
})
export class RaceService {
  constructor(private http: HttpClient) {}

  getAllRaces() {
    return this.http.get<Race[]>(`${environment.queryApiUrl}/query-service-api/races`);
  }

  getRace(id: string) {
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