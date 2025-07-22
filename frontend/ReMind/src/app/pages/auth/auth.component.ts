// ruta: frontend/ReMind/src/app/pages/auth/auth.component.ts

import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { HttpClientModule } from '@angular/common/http';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service'; // Servicio que crearemos

@Component({
  selector: 'auth-component',
  standalone: true,
  // Importamos los módulos necesarios para que funcionen ngModel, ngIf y HttpClient
  imports: [
    FormsModule,
    CommonModule,
    HttpClientModule
  ],
  templateUrl: './auth.component.html',
  styleUrls: ['./auth.component.css']
})
export class AuthComponent {
  // Objeto para vincular los datos del formulario con [(ngModel)]
  credentials = {
    usuario: '',
    contraseña: ''
  };

  errorMessage: string = '';

  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

  /**
   * Se ejecuta al enviar el formulario para iniciar sesión.
   */
  onSubmit(): void {
    this.errorMessage = ''; // Limpiar errores previos
    this.authService.login(this.credentials).subscribe({
      next: (response) => {
        console.log('Login exitoso!', response);
        // Aquí podrías guardar el token y redirigir al usuario
        // localStorage.setItem('token', response.token);
        // this.router.navigate(['/home']);
      },
      error: (err) => {
        console.error('Error en el login', err);
        this.errorMessage = 'Usuario o contraseña incorrectos.';
      }
    });
  }
}