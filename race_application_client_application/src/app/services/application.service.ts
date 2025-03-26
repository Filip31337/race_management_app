import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Application } from '../models/application.model';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class ApplicationService {

  constructor(private http: HttpClient) { }

  getAllApplications(): Observable<Application[]> {
    return this.http.get<Application[]>(`${environment.queryApiUrl}/query-service-api/applications`);
  }

  getApplication(id: string): Observable<Application> {
    return this.http.get<Application>(`${environment.queryApiUrl}/query-service-api/applications/${id}`);
  }

  createApplication(app: Partial<Application>): Observable<Application> {
    return this.http.post<Application>(`${environment.commandApiUrl}/command-service-api/applications`, app);
  }

  deleteApplication(id: string): Observable<any> {
    return this.http.delete(`${environment.commandApiUrl}/command-service-api/applications/${id}`);
  }
}
