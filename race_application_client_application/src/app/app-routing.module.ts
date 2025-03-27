import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './components/login/login.component';
import { RaceListComponent } from './components/race-list/race-list.component';
import { ApplicationListComponent } from './components/application-list/application-list.component';
import { ApplicationFormComponent } from './components/application-form/application-form.component';
import { RaceFormComponent } from './components/race-form/race-form.component';
import { AuthGuard } from './guards/auth.guard';

const routes: Routes = [
  { path: 'login', component: LoginComponent},
  { path: 'races', component: RaceListComponent },
  { 
    path: 'race/new', component: RaceFormComponent,
    canActivate: [AuthGuard],
    data: { requiredRole: 'ROLE_ADMINISTRATOR' } 
  },
  { 
    path: 'race/edit', component: RaceFormComponent,
    canActivate: [AuthGuard],
    data: { requiredRole: 'ROLE_ADMINISTRATOR' }
  },
  { 
    path: 'applications', component: ApplicationListComponent,
    canActivate: [AuthGuard],
    data: { requiredRole: 'ROLE_ADMINISTRATOR' } 
  },
  { path: 'application/new', component: ApplicationFormComponent},
  { path: 'my-applications', component: ApplicationListComponent },
  { path: '', redirectTo: '/login', pathMatch: 'full' },
  { path: '**', redirectTo: '/login' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
