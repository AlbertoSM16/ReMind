import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AgendaService {
  private apiUrl = 'http://localhost:8080/api/agenda';
  private terapeutaApiUrl = 'http://localhost:8080/api/terapeuta';


  constructor(private http: HttpClient) { }
  getAgendasByTerapeuta(terapeutaId: number): Observable<any[]> {
    return this.http.get<any[]>(`${this.terapeutaApiUrl}/${terapeutaId}/agendas`);
  }

  assignJuego(agendaId: number, juegoId: number): Observable<any> {
    return this.http.post(`${this.apiUrl}/${agendaId}/juego/${juegoId}`, {});
  }
   // OBTENER los juegos de UNA agenda
  getJuegosByAgendaId(agendaId: number): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/${agendaId}/juegos`);
  }

  // ELIMINAR un juego de una agenda
  removeJuegoFromAgenda(agendaId: number, juegoId: number): Observable<any> {
    return this.http.delete(`${this.apiUrl}/${agendaId}/juego/${juegoId}`);
  }
}