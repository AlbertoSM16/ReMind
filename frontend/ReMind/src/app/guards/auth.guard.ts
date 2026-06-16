import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';

export const authGuard: CanActivateFn = (route) => {
  const router = inject(Router);
  const token = sessionStorage.getItem('token');

  if (!token) {
    return router.parseUrl('/login');
  }

  const requiredRole = route.data?.['role'];
  if (requiredRole) {
    const userRole = sessionStorage.getItem('role');
    if (userRole !== requiredRole) {
      return router.parseUrl('/home');
    }
  }

  return true;
};
