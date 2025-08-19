
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
  juegosAsignados: any[] = [];
  pacienteId: number | null = null;
  agendaId: number | null = null;

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
        },
        error: (error) => {
          Swal.fire('Error', 'No se pudieron cargar los juegos de la agenda.', 'error');
        }
      });
    }
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