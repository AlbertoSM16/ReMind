
import { Routes } from '@angular/router';
import { AuthComponent } from './pages/auth/auth.component';

export const routes: Routes = [
  // Redirige la ruta vacía a /login
  { path: '', redirectTo: '/login', pathMatch: 'full' },
  // Carga el AuthComponent cuando la ruta es /login
  { path: 'login', component: AuthComponent },
  // Aquí puedes añadir más rutas, por ejemplo, al home.
  // { path: 'home', component: HomeComponent },
];