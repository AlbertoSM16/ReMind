import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Juego {
  id: number;
  nombre: string;
  codigo: string;
}

@Injectable({
  providedIn: 'root'
})
export class JuegoService {
  // La URL del endpoint que creaste en el backend.
  private apiUrl = 'http://localhost:8080/api/juegos';

  constructor(private http: HttpClient) { }

  /**
   * Obtiene la lista completa de todos los juegos desde el backend.
   * @returns Un Observable con un array de objetos Juego.
   */
  getJuegos(): Observable<Juego[]> {
    return this.http.get<Juego[]>(this.apiUrl);
  }
}