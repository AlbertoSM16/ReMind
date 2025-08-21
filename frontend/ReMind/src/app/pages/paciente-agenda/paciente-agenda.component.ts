import { Component, OnInit } from '@angular/core';
import { CommonModule, NgClass } from '@angular/common';
import { HeaderComponent } from '../../components/header/header.component';
import { AgendaService } from '../../services/agenda.service';
import Swal from 'sweetalert2';
import { Router } from '@angular/router';

@Component({
  selector: 'app-paciente-agenda',
  standalone: true,
  imports: [CommonModule, HeaderComponent, NgClass],
  templateUrl: './paciente-agenda.component.html',
  styleUrls: ['./paciente-agenda.component.css']
})
export class PacienteAgendaComponent implements OnInit {
  juegosAsignados: any[] = []; // Lista original sin filtrar
  juegosFiltrados: any[] = []; // Lista que se mostrará en la vista
  pacienteId: number | null = null;
  agendaId: number | null = null;
  filtroActual: 'todos' | 'completados' | 'pendientes' = 'todos'; // Estado del filtro

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
    if (this.pacienteId) {
      this.agendaService.getJuegosByPacienteId(this.pacienteId).subscribe({
        next: (data) => {
          this.agendaId = data.agendaId;
          this.juegosAsignados = data.juegos;
          this.aplicarFiltro(); 
        },
        error: (error) => {
          Swal.fire('Error', 'No se pudieron cargar los juegos de la agenda.', 'error');
        }
      });
    }
  }

  aplicarFiltro(): void {
    if (this.filtroActual === 'completados') {
      this.juegosFiltrados = this.juegosAsignados.filter(juego => juego.realizado);
    } else if (this.filtroActual === 'pendientes') {
      this.juegosFiltrados = this.juegosAsignados.filter(juego => !juego.realizado);
    } else {
      this.juegosFiltrados = this.juegosAsignados; // 'todos'
    }
  }

  cambiarFiltro(filtro: 'todos' | 'completados' | 'pendientes'): void {
    this.filtroActual = filtro;
    this.aplicarFiltro();
  }

  jugarJuego(codigo: string, dificultad: number, juegoId: number): void {
    if (this.agendaId) {
      this.router.navigate(['/juego', codigo, dificultad, this.agendaId, juegoId]);
    } else {
      Swal.fire('Error', 'No se pudo encontrar la agenda para iniciar el juego.', 'error');
    }
  }

  getGameIcon(codigo: string): string {
    const iconMap: { [key: string]: string } = {
      'matching-game': 'pi pi-th-large',
      'clothing-game': 'pi pi-user',
      'image-sequence-game': 'pi pi-sort-numeric-down',
      'intruder-game': 'pi pi-question-circle',
      'sentence-complete': 'pi pi-align-left',
      'juego-cambio': 'pi pi-euro'
    };
    return iconMap[codigo] || 'pi pi-prime';
  }
}