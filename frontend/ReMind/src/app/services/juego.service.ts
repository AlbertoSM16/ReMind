import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

export interface Juego {
  id: number;
  nombre: string;
  codigo: string;
}

@Injectable({
  providedIn: 'root'
})
export class JuegoService {
  private apiUrl = `${environment.apiUrl}/juegos`;

  constructor(private http: HttpClient) { }

  getJuegos(): Observable<Juego[]> {
    return this.http.get<Juego[]>(this.apiUrl);
  }
}
