import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HeaderComponent } from "../../components/header/header.component";
import { NavComponent } from "../../components/nav/nav.component";
import { Router } from '@angular/router';

import Swal from 'sweetalert2';
import { TerapeutaService } from '../../services/terapeuta.service';
import { Terapeuta } from '../../models/terapeuta.model';

@Component({
  selector: 'app-doctor-list',
  imports: [CommonModule, HeaderComponent, NavComponent],
  templateUrl: './doctor-list.component.html',
  styleUrl: './doctor-list.component.css'
})
export class DoctorListComponent implements OnInit {

  doctors: Terapeuta[] = [];
  isLoading = true;

  constructor(
    private router: Router,
    private terapeutaService: TerapeutaService
  ) { }

  ngOnInit(): void {
    this.getTerapeutas();
  }


  getTerapeutas(): void {
    this.isLoading = true;
    this.terapeutaService.getTerapeutas().subscribe({
      next: (data) => {
        this.doctors = data;
        this.isLoading = false;
      },
      error: (error) => {
        this.isLoading = false;
        if (error.status === 0) {
          Swal.fire('Error de conexión', 'No se puede conectar con el servidor.', 'error');
        } else {
          Swal.fire('Error', 'Ocurrió un error al cargar los doctores.', 'error');
        }
      }
    });
  }

  resetPassword(doctorId: number, doctorName: string): void {
    Swal.fire({
      title: `¿Reiniciar contraseña de ${doctorName}?`,
      text: "Se generará una nueva contraseña aleatoria. Esta acción no se puede deshacer.",
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#3085d6',
      cancelButtonColor: '#d33',
      confirmButtonText: 'Sí, reiniciar',
      cancelButtonText: 'Cancelar'
    }).then((result) => {
      if (result.isConfirmed) {
        this.terapeutaService.resetPassword(doctorId).subscribe({
          next: (response) => {
            Swal.fire({
              title: '¡Contraseña Reiniciada!',
              html: `
                    <p>La nueva contraseña para <strong>${doctorName}</strong> es:</p>
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


  showCredentials(doctorId: number): void {
    this.terapeutaService.getTerapeutaById(doctorId).subscribe({
      next: (doctor) => {
        Swal.fire({
          title: 'Datos de Acceso del Doctor',
          html: `
                <p>Las credenciales se muestran una única vez al crear el doctor por motivos de seguridad.</p>
                <div style="text-align: left; margin-top: 20px; padding: 10px; border: 1px solid #ddd; border-radius: 5px; background-color: #f9f9f9;">
                  <strong>Usuario:</strong> ${doctor.usuario}
                </div>
              `,
          icon: 'info',
          confirmButtonText: 'Entendido'
        });
      },
      error: (error) => {
        console.error('Error al obtener los datos del doctor:', error);
        Swal.fire(
          'Error',
          'No se pudieron obtener los datos del doctor.',
          'error'
        );
      }
    });
  }


  deleteDoctor(terapeutaId: number): void {
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
        this.terapeutaService.deleteTerapeuta(terapeutaId).subscribe({
          next: () => {
            this.doctors = this.doctors.filter(p => p.id !== terapeutaId);
            Swal.fire(
              '¡Eliminado!',
              'El doctor ha sido eliminado.',
              'success'
            );
          },
          error: (error) => {
            console.error('Error al eliminar doctor:', error);
            Swal.fire(
              'Error',
              'Hubo un problema al eliminar el doctor.',
              'error'
            );
          }
        });
      }
    });
  }

  goEditDoctor(terapeutaId: number): void {
    this.router.navigate(['/doctors/edit', terapeutaId]);
  }

  goToCreateDoctor(): void {
    this.router.navigate(['/doctors/new']);
  }
}
