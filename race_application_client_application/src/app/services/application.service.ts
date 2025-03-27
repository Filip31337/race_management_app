import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
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
          { id: 'a2', firstName: 'Bob', lastName: 'Jones', raceId: '2' }
        ];
        return of(mockApplications);
      }
    return this.http.get<Application[]>(`${environment.queryApiUrl}/query-service-api/applications`);
  }

  getApplication(id: string): Observable<Application> {
    if (!environment.production) {
        const mockApp: Application = { id, firstName: 'Mock', lastName: 'User', raceId: '1' };
        return of(mockApp);
      }
    return this.http.get<Application>(`${environment.queryApiUrl}/query-service-api/applications/${id}`);
  }

  createApplication(app: Partial<Application>): Observable<Application> {
    return this.http.post<Application>(`${environment.commandApiUrl}/command-service-api/applications`, app);
  }

  deleteApplication(id: string): Observable<any> {
    return this.http.delete(`${environment.commandApiUrl}/command-service-api/applications/${id}`);
  }
}
