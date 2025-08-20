import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HeaderComponent } from '../../components/header/header.component';
import { TerapeutaService, TerapeutaSeguimiento } from '../../services/terapeuta.service';
import Swal from 'sweetalert2';
import { Router } from '@angular/router';
import { ButtonModule } from 'primeng/button';

@Component({
  selector: 'app-follow',
  standalone: true,
  imports: [CommonModule, HeaderComponent, ButtonModule],
  templateUrl: './follow.component.html',
  styleUrls: ['./follow.component.css']
})
export class FollowComponent implements OnInit {

  terapeutaData: TerapeutaSeguimiento | null = null;
  isLoading = true;
  // --- NUEVO: Variable para controlar qué paciente está expandido ---
  expandedPacienteId: number | null = null;

  constructor(
    private terapeutaService: TerapeutaService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.loadSeguimientoData();
  }

  loadSeguimientoData(): void {
    const terapeutaIdString = sessionStorage.getItem('id');
    const userRole = sessionStorage.getItem('role');

    if (userRole !== 'terapeuta' || !terapeutaIdString) {
      this.isLoading = false;
      Swal.fire('Acceso Denegado', 'Esta sección es solo para terapeutas.', 'error');
      this.router.navigate(['/home']);
      return;
    }

    const terapeutaId = +terapeutaIdString;
    this.isLoading = true;

    this.terapeutaService.getSeguimiento(terapeutaId).subscribe({
      next: (data) => {
        this.isLoading = false;
        if (data && data.pacientes && data.pacientes.length > 0) {
          this.terapeutaData = data;
        } else {
          Swal.fire({
            title: 'Sin Pacientes',
            text: 'Actualmente no tienes pacientes asignados para realizar seguimiento.',
            icon: 'info',
            confirmButtonText: 'Volver al inicio'
          }).then(() => {
            this.router.navigate(['/home']);
          });
        }
      },
      error: (err) => {
        console.error('Error al cargar los datos de seguimiento', err);
        this.isLoading = false;
        Swal.fire('Error', 'No se pudieron cargar los datos de seguimiento.', 'error');
      }
    });
  }

  calculateProgress(completados: number, totales: number): number {
    if (totales === 0) {
      return 0;
    }
    return (completados / totales) * 100;
  }

  // --- NUEVA FUNCIÓN: Para mostrar/ocultar los juegos del paciente ---
  toggleJuegos(pacienteId: number): void {
    if (this.expandedPacienteId === pacienteId) {
      this.expandedPacienteId = null; // Si ya está abierto, lo cierra
    } else {
      this.expandedPacienteId = pacienteId; // Si no, lo abre
    }
  }

  getDificultadTexto(dificultad: number): string {
    switch (dificultad) {
      case 1: return 'Fácil';
      case 2: return 'Media';
      case 3: return 'Difícil';
      default: return 'No definida';
    }
  }
}