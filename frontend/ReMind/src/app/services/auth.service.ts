import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = 'http://localhost:8080/api/auth/login';

  constructor(private http: HttpClient) { }

  /**
   * Envía las credenciales al backend para iniciar sesión.
   * @param credentials Un objeto con 'usuario' y 'codigoAcceso'.
   * @returns Un Observable con la respuesta del servidor.
   */
  login(credentials: any): Observable<any> {
    return this.http.post(this.apiUrl, credentials);
  }
}