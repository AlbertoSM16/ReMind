// ruta: frontend/ReMind/src/app/pages/auth/auth.component.ts

import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { HttpClientModule } from '@angular/common/http';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'auth-component',
  standalone: true,
  // Importamos los módulos necesarios para el formulario y las peticiones HTTP
  imports: [FormsModule, CommonModule, HttpClientModule],
  templateUrl: './auth.component.html',
  styleUrls: ['./auth.component.css']
})
export class AuthComponent {
  credentials = {
    usuario: '',
    contraseña: ''
  };

  errorMessage: string = '';

  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

  onSubmit(): void {
    this.errorMessage = '';
    this.authService.login(this.credentials).subscribe({
      next: (response) => {
        console.log('Login exitoso!', response);
        // Aquí puedes guardar el token (ej. en localStorage)
        // localStorage.setItem('token', response.token);
        // Y redirigir al usuario
        // this.router.navigate(['/home']);
      },
      error: (err) => {
        console.error('Error en el login', err);
        this.errorMessage = 'Usuario o contraseña incorrectos.';
      }
    });
  }
}