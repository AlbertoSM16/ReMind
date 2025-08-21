// ruta: frontend/ReMind/src/app/pages/auth/auth.component.ts

import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { HttpClientModule } from '@angular/common/http';
import { Router,RouterModule} from '@angular/router';
import { AuthService } from '../../services/auth.service'; // Servicio que crearemos

@Component({
  selector: 'auth-component',
  standalone: true,
  imports: [
    FormsModule,
    CommonModule,
    HttpClientModule,
    RouterModule
  ],
  templateUrl: './auth.component.html',
  styleUrls: ['./auth.component.css']
})
export class AuthComponent {
  credentials = {
    usuario: '',
    contrasenia: ''
  };

  errorMessage: string = '';

  constructor(
    private authService: AuthService,
    private router: Router
  ) {}


  onLogin(): void {
    this.errorMessage = ''; 
    this.authService.login(this.credentials).subscribe({
      next: (response) => {
        sessionStorage.setItem('role',response.rol);
        sessionStorage.setItem('token', response.token);
        sessionStorage.setItem('id', response.id);
        sessionStorage.setItem('nombre', response.nombre);
        this.router.navigate(['/home']);
      },
      error: (err) => {
        this.errorMessage = 'Usuario o contraseña incorrectos.';
      }
    });
  }
}