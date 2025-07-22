import { Component } from '@angular/core';
import { AuthService } from '../../services/auth.service'; 
import { Router } from '@angular/router'; 
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-auth',
  templateUrl: './auth.component.html',
  styleUrls: ['./auth.component.css']
})
export class AuthComponent {
  credentials = {
    usuario: '',
    codigoAcceso: ''
  };

  errorMessage: string = '';

  constructor(
    private authService: AuthService,
    private router: Router 
  ) { }

 
  onSubmit(): void {
    this.errorMessage = '';
    this.authService.login(this.credentials).subscribe({
      next: (response) => {
        console.log('Login exitoso!', response);
        
        localStorage.setItem('token', response.token);

        // this.router.navigate(['/dashboard']);
      },
      error: (err) => {
        console.error('Error en el login', err);
        this.errorMessage = 'Usuario o código de acceso incorrecto. Por favor, inténtalo de nuevo.';
      }
    });
  }
}