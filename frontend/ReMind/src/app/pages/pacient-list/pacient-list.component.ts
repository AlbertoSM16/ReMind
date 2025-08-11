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