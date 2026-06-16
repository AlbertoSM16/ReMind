import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HeaderComponent } from '../../components/header/header.component';
import { AgendaService } from '../../services/agenda.service';
import Swal from 'sweetalert2';
import { Router } from '@angular/router';

@Component({
  selector: 'app-paciente-agenda',
  standalone: true,
  imports: [CommonModule, HeaderComponent],
  templateUrl: './paciente-agenda.component.html',
  styleUrls: ['./paciente-agenda.component.css']
})
export class PacienteAgendaComponent implements OnInit {
  juegosAsignados: any[] = [];
  juegosPendientes: any[] = [];
  juegosCompletados: any[] = [];
  pacienteId: number | null = null;
  agendaId: number | null = null;
  isLoading = true;

  constructor(
    private agendaService: AgendaService,
    private router: Router
  ) { }

  ngOnInit(): void {
    const id = sessionStorage.getItem('id');
    if (id) {
      this.pacienteId = +id;
      this.loadJuegos();
    } else {
      Swal.fire('Error', 'No se pudo identificar al paciente.', 'error');
      this.router.navigate(['/login']);
    }
  }

  loadJuegos(): void {
    this.isLoading = true;
    if (this.pacienteId) {
      this.agendaService.getJuegosByPacienteId(this.pacienteId).subscribe({
        next: (data) => {
          this.agendaId = data.agendaId;
          this.juegosAsignados = data.juegos;
          this.juegosPendientes = data.juegos.filter((j: any) => !j.realizado);
          this.juegosCompletados = data.juegos.filter((j: any) => j.realizado);
          this.isLoading = false;
        },
        error: () => {
          this.isLoading = false;
          Swal.fire('Error', 'No se pudieron cargar los juegos.', 'error');
        }
      });
    }
  }

  jugarJuego(codigo: string, dificultad: number, juegoId: number): void {
    if (this.agendaId) {
      this.router.navigate(['/juego', codigo, dificultad, this.agendaId, juegoId]);
    } else {
      Swal.fire('Error', 'No se pudo encontrar la agenda.', 'error');
    }
  }

  getGameIcon(codigo: string): string {
    const iconMap: { [key: string]: string } = {
      'matching-game': 'bi bi-puzzle-fill',
      'clothing-game': 'bi bi-person-check-fill',
      'image-sequence-game': 'bi bi-images',
      'intruder-game': 'bi bi-search-heart',
      'sentence-complete': 'bi bi-chat-left-text-fill',
      'juego-cambio': 'bi bi-currency-euro'
    };
    return iconMap[codigo] || 'bi bi-play-circle-fill';
  }
}
