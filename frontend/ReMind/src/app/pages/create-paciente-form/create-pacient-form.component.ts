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

    const terapeutaId = sessionStorage.getItem('id');
    if (!terapeutaId) {
      Swal.fire('Error', 'No se pudo identificar al terapeuta. Por favor, inicie sesión de nuevo.', 'error');
      return;
    }
    this.pacient.terapeuta_id = +terapeutaId;

    // Llama al servicio para crear el paciente
    this.pacientService.createPatient(this.pacient).subscribe({
      next: (response) => {
        // Muestra los datos de acceso en un diálogo de SweetAlert2
        Swal.fire({
          title: '¡Paciente Creado!',
          html: `
            <p>El paciente ha sido creado con éxito. Sus datos de acceso son:</p>
            <div style="text-align: left; margin-left: 20px;">
              <strong>Usuario:</strong> ${response.usuario}<br>
              <strong>Contraseña:</strong> ${response.contrasenia}
            </div>
            <br>
            <p>Por favor, guarde estas credenciales en un lugar seguro.</p>
          `,
          icon: 'success',
          confirmButtonText: 'Entendido'
        }).then(() => {
          // Redirige a la lista de pacientes después de cerrar el diálogo
          this.router.navigate(['/pacients']);
        });
      },
      error: (error) => {
        console.error('Error al crear paciente:', error);
        Swal.fire(
          'Error',
          'Hubo un problema al crear el paciente.',
          'error'
        );
      }
    });
  }
}