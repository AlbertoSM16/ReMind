// src/app/services/pacient.service.ts
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Paciente as Patient } from '../models/paciente.model'; 

@Injectable({
  providedIn: 'root'
})

export class PacientServiceService { // As per your component's import
  private apiUrl = 'http://localhost:8080/api/paciente';

  constructor(private http: HttpClient) { }

  /**
   * Sends a POST request to create a new patient.
   * @param patient The patient object to create.
   * @returns An Observable of the HTTP response.
   */
  createPatient(patient: Patient): Observable<Patient> {
    return this.http.post<Patient>(this.apiUrl, patient);
  }

}