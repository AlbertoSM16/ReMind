
import { Routes } from '@angular/router';
import { AuthComponent } from './pages/auth/auth.component';
import { HomeComponent } from './pages/home/home.component';
import { PacientListComponent } from './pages/pacient-list/pacient-list.component';
import { CreatePatientFormComponent } from './pages/create-paciente-form/create-pacient-form.component';
import { AgendaListComponent } from './pages/agenda-list/agenda-list.component';
import { AgendaDetailComponent } from './pages/agenda-detail/agenda-detail.component';
import { EditPatientFormComponent } from './pages/edit-patient/edit-patient.component';



export const routes: Routes = [
  { path: '', redirectTo: '/login', pathMatch: 'full' },

  { path: 'login', component: AuthComponent },
  
  { path: 'home', component: HomeComponent },

  { path: 'pacients', component: PacientListComponent },

  { path: 'create-patient', component: CreatePatientFormComponent },

  { path: 'agendas', component: AgendaListComponent },

  { path: 'edit-patient/:id', component: EditPatientFormComponent },


  { path: 'agenda/:id', component: AgendaDetailComponent }
];