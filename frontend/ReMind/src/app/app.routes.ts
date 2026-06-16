import { Routes } from '@angular/router';
import { AuthComponent } from './pages/auth/auth.component';
import { HomeComponent } from './pages/home/home.component';
import { PacientListComponent } from './pages/pacient-list/pacient-list.component';
import { CreatePatientFormComponent } from './pages/create-paciente-form/create-pacient-form.component';
import { AgendaListComponent } from './pages/agenda-list/agenda-list.component';
import { AgendaDetailComponent } from './pages/agenda-detail/agenda-detail.component';
import { EditPatientFormComponent } from './pages/edit-patient/edit-patient.component';
import { PacienteAgendaComponent } from './pages/paciente-agenda/paciente-agenda.component';
import { Gameplay } from './pages/gameplay/gameplay.component';
import { LandingPageComponent } from './pages/landing-page/landing-page.component';
import { FollowComponent } from './pages/follow/follow.component';
import { DoctorListComponent } from './pages/doctor-list/doctor-list.component';
import { DoctorFormComponent } from './pages/doctor-form/doctor-form.component';
import { authGuard } from './guards/auth.guard';

export const routes: Routes = [
  { path: '', redirectTo: '/landing', pathMatch: 'full' },

  { path: 'login', component: AuthComponent },
  { path: 'landing', component: LandingPageComponent },

  { path: 'home', component: HomeComponent, canActivate: [authGuard] },

  { path: 'pacients', component: PacientListComponent, canActivate: [authGuard], data: { role: 'terapeuta' } },

  { path: 'create-patient', component: CreatePatientFormComponent, canActivate: [authGuard], data: { role: 'terapeuta' } },

  { path: 'agendas', component: AgendaListComponent, canActivate: [authGuard], data: { role: 'terapeuta' } },

  { path: 'edit-patient/:id', component: EditPatientFormComponent, canActivate: [authGuard], data: { role: 'terapeuta' } },

  { path: 'agenda/:id', component: AgendaDetailComponent, canActivate: [authGuard], data: { role: 'terapeuta' } },

  { path: 'tareas', redirectTo: '/paciente-agenda', pathMatch: 'full' },
  { path: 'paciente-agenda', component: PacienteAgendaComponent, canActivate: [authGuard], data: { role: 'paciente' } },

  { path: 'juego/:codigo/:dificultad/:agendaId/:juegoId', component: Gameplay, canActivate: [authGuard], data: { role: 'paciente' } },

  { path: 'follow', component: FollowComponent, canActivate: [authGuard], data: { role: 'terapeuta' } },

  { path: 'edit-agenda/:id', redirectTo: '/agendas', pathMatch: 'full' },
  { path: 'doctors/new', component: DoctorFormComponent, canActivate: [authGuard], data: { role: 'administrador' } },

  { path: 'doctors/edit/:id', component: DoctorFormComponent, canActivate: [authGuard], data: { role: 'administrador' } },
  { path: 'doctors', component: DoctorListComponent, canActivate: [authGuard], data: { role: 'administrador' } }
];
