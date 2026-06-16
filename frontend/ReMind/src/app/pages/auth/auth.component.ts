import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Router, RouterModule } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'auth-component',
  standalone: true,
  imports: [
    FormsModule,
    CommonModule,
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
  isLoading = false;
  submitted = false;

  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

  onLogin(): void {
    this.submitted = true;
    this.errorMessage = '';

    if (!this.credentials.usuario || !this.credentials.contrasenia) {
      return;
    }

    this.isLoading = true;

    this.authService.login(this.credentials).subscribe({
      next: (response) => {
        this.isLoading = false;
        sessionStorage.setItem('role', response.rol);
        sessionStorage.setItem('token', response.token);
        sessionStorage.setItem('id', response.id);
        sessionStorage.setItem('nombre', response.nombre);
        this.router.navigate(['/home']);
      },
      error: () => {
        this.isLoading = false;
        this.errorMessage = 'Usuario o contraseña incorrectos.';
      }
    });
  }
}
