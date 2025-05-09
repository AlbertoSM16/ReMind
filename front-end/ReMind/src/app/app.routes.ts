import { Routes } from '@angular/router';

export const routes: Routes = [
    {
        path: '',
        loadComponent: () => import('./pages/home/home.component').then(m => m.HomeComponent),
    },
    {
        path: 'login',
        loadComponent: () => import ('./pages/auth/auth.component').then(m => m.AuthComponent),
    },
    {
        path: 'landingPage',
        loadComponent: () => import ('./pages/lading-page/lading-page.component').then(m => m.LadingPageComponent),
    }
];
