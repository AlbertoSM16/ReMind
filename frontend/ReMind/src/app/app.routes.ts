
import { Routes } from '@angular/router';
import { AuthComponent } from './pages/auth/auth.component';
import { HomeComponent } from './pages/home/home.component';
import { PacientListComponent } from './pages/pacient-list/pacient-list.component';
import { CreatePatientFormComponent } from './pages/create-paciente-form/create-pacient-form.component';
import { AgendaListComponent } from './pages/agenda-list/agenda-list.component';
import { AgendaDetailComponent } from './pages/agenda-detail/agenda-detail.component';



export const routes: Routes = [
  // Redirige la ruta vacía a /login
  { path: '', redirectTo: '/login', pathMatch: 'full' },
  // Carga el AuthComponent cuando la ruta es /login
  { path: 'login', component: AuthComponent },
  // Aquí puedes añadir más rutas, por ejemplo, al home.
  { path: 'home', component: HomeComponent },

  {path: 'pacients', component: PacientListComponent},
  
  {path : 'create-patient', component: CreatePatientFormComponent},

  { path: 'agendas', component: AgendaListComponent },

  { path: 'agenda/:id', component: AgendaDetailComponent }
];