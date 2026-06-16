import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Paciente as Patient } from '../models/paciente.model';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})

export class PacientService {
  private apiUrl = `${environment.apiUrl}/paciente`;

  constructor(private http: HttpClient) { }

  createPatient(patient: Patient): Observable<Patient> {
    return this.http.post<Patient>(this.apiUrl, patient);
  }

  getPatients(): Observable<Patient[]> {
    return this.http.get<Patient[]>(this.apiUrl);
  }

  getPatientById(id: number): Observable<Patient> {
    return this.http.get<Patient>(`${this.apiUrl}/${id}`);
  }

  updatePatient(id: number, patient: Patient): Observable<Patient> {
    return this.http.put<Patient>(`${this.apiUrl}/${id}`, patient);
  }

  deletePatient(id: number): Observable<any> {
    return this.http.delete(`${this.apiUrl}/${id}`, { responseType: 'text' });
  }

  resetPassword(id: number): Observable<{ nuevaContrasenia: string }> {
    return this.http.post<{ nuevaContrasenia: string }>(`${this.apiUrl}/${id}/reset-password`, {});
  }
}
