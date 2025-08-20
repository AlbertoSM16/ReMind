import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

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


    getSeguimiento(terapeutaId: number): Observable<TerapeutaSeguimiento> {
        return this.http.get<TerapeutaSeguimiento>(`${this.apiUrl}/${terapeutaId}/seguimiento`);
    }
}