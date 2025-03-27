import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { Application } from '../models/application.model';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class ApplicationService {

  constructor(private http: HttpClient) { }

  getAllApplications(): Observable<Application[]> {
    if (!environment.production) {
        const mockApplications: Application[] = [
          { id: 'a1', firstName: 'Alice', lastName: 'Smith', raceId: '1' },
          { id: 'a2', firstName: 'Bob', lastName: 'Jones', raceId: '2' },
          { id: 'a3', firstName: 'Alice', lastName: 'Smith', raceId: '1' },
          { id: 'a4', firstName: 'Bob', lastName: 'Jones', raceId: '2' },
          { id: 'a5', firstName: 'Luisa', lastName: 'McGuire', raceId: '1' },
          { id: 'a6', firstName: 'Darren', lastName: 'Smith', raceId: '1' },
          { id: 'a7', firstName: 'Isabella', lastName: 'Fernandez', raceId: '1' },
          { id: 'a8', firstName: 'Emory', lastName: 'Coffey', raceId: '2' }
        ];
        return of(mockApplications);
    }
    return this.http.get<Application[]>(`${environment.queryApiUrl}/query-service-api/applications`);
  }

  getMyApplications(): Observable<Application[]> {
    if (!environment.production) {
      const mockApplications: Application[] = [
        { id: 'a1', firstName: 'John', lastName: 'Smith', raceId: '1' },
        { id: 'a2', firstName: 'John ', lastName: 'Smith', raceId: '2' }
      ];
      return of(mockApplications);
  }
    return this.http.get<Application[]>(`${environment.queryApiUrl}/query-service-api/applications/user`);
  }

  getApplication(id: string): Observable<Application> {
    if (!environment.production) {
        const mockApp: Application = { id, firstName: 'Mock', lastName: 'User', raceId: '1' };
        return of(mockApp);
      }
    return this.http.get<Application>(`${environment.queryApiUrl}/query-service-api/applications/${id}`);
  }

  createApplication(app: Partial<Application>): Observable<Application> {
    const params = new HttpParams()
    .set('firstName', app.firstName || '')
    .set('lastName', app.lastName || '')
    .set('club', app.club || '')
    .set('raceId', app.raceId || '');
    return this.http.post<Application>(`${environment.commandApiUrl}/command-service-api/applications`, null, { params });
  }

  deleteApplication(id: string): Observable<any> {
    return this.http.delete(`${environment.commandApiUrl}/command-service-api/applications/${id}`);
  }
}
