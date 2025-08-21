
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
import { LadingPageComponent } from './pages/landing-page/landing-page.component';
import { FollowComponent } from './pages/follow/follow.component';



export const routes: Routes = [
  { path: '', redirectTo: '/landing', pathMatch: 'full' },

  { path: 'login', component: AuthComponent },
  
  { path: 'landing', component: LadingPageComponent },
  
  { path: 'home', component: HomeComponent },

  { path: 'pacients', component: PacientListComponent },

  { path: 'create-patient', component: CreatePatientFormComponent },

  { path: 'agendas', component: AgendaListComponent },

  { path: 'edit-patient/:id', component: EditPatientFormComponent },

  { path: 'agenda/:id', component: AgendaDetailComponent },

  { path: 'paciente-agenda', component: PacienteAgendaComponent },

  { path: 'juego/:codigo/:dificultad/:agendaId/:juegoId', component: Gameplay },

  {path: 'follow', component: FollowComponent} 

];