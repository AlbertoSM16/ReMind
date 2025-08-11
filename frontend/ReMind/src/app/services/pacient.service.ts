// src/app/services/pacient.service.ts
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Paciente as Patient } from '../models/paciente.model'; 

@Injectable({
  providedIn: 'root'
})

export class PacientService { // As per your component's import
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


  /**
   * Sends a GET request to retrieve all patients.
   * @returns An Observable of the list of patients.
   */
  getPatients(): Observable<Patient[]> {
    return this.http.get<Patient[]>(this.apiUrl);
  }
  /**
   * Sends a GET request to retrieve a patient by ID.
   * @param id The ID of the patient to retrieve.
   * @returns An Observable of the patient object.
   */
  getPatientById(id: number): Observable<Patient> {
    return this.http.get<Patient>(`${this.apiUrl}/${id}`);
  }

  /**
   * 
   */
  updatePatient(id: number, patient: Patient): Observable<Patient> {

    return this.http.put<Patient>(`${this.apiUrl}/${id}`, patient);
  }

   deletePatient(id: number): Observable<any> {
    return this.http.delete(`${this.apiUrl}/${id}`, { responseType: 'text' });
  }
}



