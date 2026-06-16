import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class AgendaService {
  private baseApiUrl = environment.apiUrl;

  constructor(private http: HttpClient) { }

  getAgendasByTerapeuta(terapeutaId: number): Observable<any[]> {
    return this.http.get<any[]>(`${this.baseApiUrl}/terapeuta/${terapeutaId}/agendas`);
  }

  assignJuego(agendaId: number, juegoId: number, dificultad: number): Observable<any> {
    const body = { juegoId, dificultad };
    return this.http.post(`${this.baseApiUrl}/agenda/${agendaId}/juego`, body);
  }

  getJuegosByAgendaId(agendaId: number): Observable<any[]> {
    return this.http.get<any[]>(`${this.baseApiUrl}/agenda/${agendaId}/juegos`);
  }

  getJuegosByPacienteId(pacienteId: number): Observable<any> {
    return this.http.get<any>(`${this.baseApiUrl}/agenda/paciente/${pacienteId}`);
  }

  completarJuego(agendaId: number, juegoId: number): Observable<any> {
    return this.http.put(`${this.baseApiUrl}/agenda/${agendaId}/juego/${juegoId}/completar`, {});
  }

  removeJuegoFromAgenda(agendaId: number, juegoId: number): Observable<any> {
    return this.http.delete(`${this.baseApiUrl}/agenda/${agendaId}/juego/${juegoId}`);
  }
}
