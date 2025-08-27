import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Terapeuta } from '../models/terapeuta.model';

export interface PacienteSeguimiento {
    pacienteId: number;
    nombre: string;
    juegosCompletados: number;
    juegosTotales: number;
    juegos: JuegoAsignado[];

}
export interface JuegoAsignado {
    nombre: string;
    dificultad: number;
    realizado: boolean;
}

export interface TerapeutaSeguimiento {
    id: number;
    nombre: string;
    pacientes: PacienteSeguimiento[];
}

@Injectable({
    providedIn: 'root'
})
export class TerapeutaService {
    private apiUrl = 'http://localhost:8080/api/terapeuta';

    constructor(private http: HttpClient) { }

    getTerapeutas(): Observable<Terapeuta[]> {
        return this.http.get<Terapeuta[]>(this.apiUrl);
    }

    getTerapeutaById(id: number): Observable<Terapeuta> {
        return this.http.get<Terapeuta>(`${this.apiUrl}/${id}`);
    }

    createTerapeuta(terapeuta: Terapeuta): Observable<Terapeuta> {
        return this.http.post<Terapeuta>(this.apiUrl, terapeuta);
    }

    updateTerapeuta(id: number, terapeuta: Terapeuta): Observable<Terapeuta> {
        return this.http.put<Terapeuta>(`${this.apiUrl}/${id}`, terapeuta);
    }

    deleteTerapeuta(id: number): Observable<void> {
        return this.http.delete<void>(`${this.apiUrl}/${id}`);
    }

    getSeguimiento(terapeutaId: number): Observable<TerapeutaSeguimiento> {
        return this.http.get<TerapeutaSeguimiento>(`${this.apiUrl}/${terapeutaId}/seguimiento`);
    }

    resetPassword(id: number): Observable<{ nuevaContrasenia: string }> {
        return this.http.post<{ nuevaContrasenia: string }>(`${this.apiUrl}/${id}/reset-password`, {});
    }
}