import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { PacientService } from '../../services/pacient.service'; // Ensure this path is correct
import { Router } from '@angular/router';
import { Paciente } from '../../models/paciente.model';
import Swal from 'sweetalert2';

export interface Patient {
  nombre: string;
  apellido: string;   
  email: string;
  contrasenia: string;
  telefono: string;
  enfermedad: string; 
  edad: number;
  nombreResponsable: string;
  fechaNacimiento: string; 
  terapeuta_id: number;
}

@Component({
  selector: 'app-edit-patient-form', 
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './create-pacient-form.component.html',
  styleUrls: ['./create-pacient-form.component.css']
})
export class CreatePatientFormComponent implements OnInit {
  pacient: Paciente = {} as Paciente;

  constructor(
    private pacientService: PacientService,
    private router: Router
  ) { }

  ngOnInit(): void {
  }

  onSubmit(): void {

    const terapeutaId = localStorage.getItem('id');
    if (!terapeutaId) {
      return;
    }
    this.pacient.terapeuta_id = +terapeutaId;
    this.pacientService.createPatient(this.pacient).subscribe({
      next: (response) => {
        Swal.fire(
          '¡Paciente creado!',
          'El paciente ha sido creado.',
          'success'
        ); this.router.navigate(['/pacients']);
      },
      error: (error) => {
        console.error('Error al crear paciente:', error);
        const errorMessage = error.error && error.error.message ? error.error.message : 'Error al crear paciente. Por favor, intente de nuevo.';
        alert(errorMessage);
      }
    });
  }
}