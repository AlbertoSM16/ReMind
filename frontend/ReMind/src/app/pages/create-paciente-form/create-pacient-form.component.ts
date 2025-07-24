import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { PacientServiceService } from '../../services/pacient.service'; // Ensure this path is correct
import { Router } from '@angular/router';
import { Paciente } from '../../models/paciente.model';

export interface Patient {
  nombre: string;
  apellido: string; // Changed from 'apellidos' to 'apellido'
  email: string;
  contrasenia: string;
  telefono: string;
  enfermedad: string; // Changed from 'diagnostico' to 'enfermedad'
  edad: number;
  nombreResponsable: string;
  fechaNacimiento: string; // Use string for date input (YYYY-MM-DD format)
  terapeuta_id: number;
}

@Component({
  selector: 'app-edit-patient-form', // Keeping the original selector as requested
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './create-pacient-form.component.html',
  styleUrls: ['./create-pacient-form.component.css']
})
export class CreatePatientFormComponent implements OnInit {
  pacient : Paciente = {} as Paciente;

  constructor(
    private pacientService: PacientServiceService,
    private router: Router
  ) { }

  ngOnInit(): void {
    // No initial data loading needed for creating a patient
  }

  onSubmit(): void {
    // Before sending, you might want to convert date to the backend's expected format if it's not already
    // For example, if backend expects Date object from string:
    // const patientDataToSend = {
    //   ...this.patient,
    //   fechaNacimiento: new Date(this.patient.fechaNacimiento)
    // };

    this.pacientService.createPatient(this.pacient).subscribe({
      next: (response) => {
        console.log('Paciente creado exitosamente:', response);
        alert('Paciente creado exitosamente!');
        // Navigate to the patient list or clear the form
        this.router.navigate(['/pacients']); 
      },
      error: (error) => {
        console.error('Error al crear paciente:', error);
        // Check if the error response has a specific message from the backend
        const errorMessage = error.error && error.error.message ? error.error.message : 'Error al crear paciente. Por favor, intente de nuevo.';
        alert(errorMessage);
      }
    });
  }
}