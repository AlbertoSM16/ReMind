import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HeaderComponent } from "../../components/header/header.component";
import { NavComponent } from "../../components/nav/nav.component";
import { Router } from '@angular/router';
import { PacientService } from '../../services/pacient.service';
import { Paciente } from '../../models/paciente.model';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-pacient-list',
  standalone: true,
  imports: [CommonModule, HeaderComponent, NavComponent],
  templateUrl: './pacient-list.component.html',
  styleUrls: ['./pacient-list.component.css']
})
export class PacientListComponent implements OnInit {

  patients: Paciente[] = [];

  constructor(
    private router: Router,
    private pacientService: PacientService
  ) { }

  ngOnInit(): void {
    this.getPacients();
  }

  goToCreatePatient(): void {
    this.router.navigate(['/create-patient']);
  }

  getPacients(): void {
    this.pacientService.getPatients().subscribe({
      next: (data) => {
        this.patients = data;

      },
      error: (error) => {
        Swal.fire({
          title: 'No se encontraron pacientes',
          text: 'Actualmente no hay pacientes registrados.',
          icon: 'info',
          confirmButtonColor: '#61b369'
        });
      }
    });
  }
  editPatient(pacientId: number): void {
    this.router.navigate(['/edit-patient', pacientId]);
  }
  resetPassword(patientId: number, patientName: string): void {
    Swal.fire({
      title: `¿Reiniciar contraseña de ${patientName}?`,
      text: "Se generará una nueva contraseña aleatoria. Esta acción no se puede deshacer.",
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#3085d6',
      cancelButtonColor: '#d33',
      confirmButtonText: 'Sí, reiniciar',
      cancelButtonText: 'Cancelar'
    }).then((result) => {
      if (result.isConfirmed) {
        this.pacientService.resetPassword(patientId).subscribe({
          next: (response) => {
            Swal.fire({
              title: '¡Contraseña Reiniciada!',
              html: `
                <p>La nueva contraseña para <strong>${patientName}</strong> es:</p>
                <div style="background-color: #f0f0f0; padding: 10px; border-radius: 5px; font-weight: bold; font-size: 1.2em; margin-top: 15px; user-select: all;">
                  ${response.nuevaContrasenia}
                </div>
                <p style="margin-top: 15px;">Por favor, anótala y entrégasela al paciente.</p>
              `,
              icon: 'success'
            });
          },
          error: (err) => {
            console.error('Error al reiniciar la contraseña:', err);
            Swal.fire('Error', 'No se pudo reiniciar la contraseña.', 'error');
          }
        });
      }
    });
  }


  showCredentials(patientId: number): void {
    this.pacientService.getPatientById(patientId).subscribe({
      next: (patient) => {
        Swal.fire({
          title: 'Datos de Acceso del Paciente',
          html: `
            <p>Las credenciales se muestran una única vez al crear el paciente por motivos de seguridad.</p>
            <div style="text-align: left; margin-top: 20px; padding: 10px; border: 1px solid #ddd; border-radius: 5px; background-color: #f9f9f9;">
              <strong>Usuario:</strong> ${patient.usuario}
            </div>
          `,
          icon: 'info',
          confirmButtonText: 'Entendido'
        });
      },
      error: (error) => {
        console.error('Error al obtener los datos del paciente:', error);
        Swal.fire(
          'Error',
          'No se pudieron obtener los datos del paciente.',
          'error'
        );
      }
    });
  }


  deletePatient(pacientId: number): void {
    Swal.fire({
      title: '¿Estás seguro?',
      text: "¡No podrás revertir esta acción!",
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#d33',
      cancelButtonColor: '#3085d6',
      confirmButtonText: 'Sí, ¡eliminar!',
      cancelButtonText: 'Cancelar'
    }).then((result) => {
      if (result.isConfirmed) {
        // Si el usuario confirma, se procede a eliminar
        this.pacientService.deletePatient(pacientId).subscribe({
          next: () => {
            this.patients = this.patients.filter(p => p.id !== pacientId);
            Swal.fire(
              '¡Eliminado!',
              'El paciente ha sido eliminado.',
              'success'
            );
          },
          error: (error) => {
            console.error('Error al eliminar paciente:', error);
            Swal.fire(
              'Error',
              'Hubo un problema al eliminar el paciente.',
              'error'
            );
          }
        });
      }
    });
  }
}