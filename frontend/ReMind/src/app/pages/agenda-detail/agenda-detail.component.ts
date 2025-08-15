import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HeaderComponent } from '../../components/header/header.component';
import { AgendaService } from '../../services/agenda.service';
import { JuegoService } from '../../services/juego.service';
import Swal from 'sweetalert2';
import { forkJoin } from 'rxjs';

@Component({
  selector: 'app-agenda-detail',
  standalone: true,
  imports: [CommonModule, HeaderComponent, FormsModule],
  templateUrl: './agenda-detail.component.html',
  styleUrls: ['./agenda-detail.component.css']
})
export class AgendaDetailComponent implements OnInit {

  agendaId!: number;
  juegosAsignados: any[] = [];
  juegosDisponibles: any[] = [];
  todosLosJuegos: any[] = [];
  juegoSeleccionadoId: number | null = null;
  dificultadSeleccionada: number = 1;
  isLoading: boolean = false;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private agendaService: AgendaService,
    private juegoService: JuegoService
  ) { }

  ngOnInit(): void {
    this.agendaId = +this.route.snapshot.paramMap.get('id')!;
    this.cargarDatos();
  }

  cargarDatos(): void {
    this.isLoading = true;
    forkJoin({
      todos: this.juegoService.getJuegos(),
      asignados: this.agendaService.getJuegosByAgendaId(this.agendaId)
    }).subscribe({
      next: ({ todos, asignados }) => {
        this.todosLosJuegos = todos;
        this.juegosAsignados = asignados;
        this.actualizarJuegosDisponibles();
        this.isLoading = false;
      },
      error: (err) => {
        Swal.fire('Error', 'No se pudieron cargar los datos de la agenda.', 'error');
        this.isLoading = false;
      }
    });
  }

  actualizarJuegosDisponibles(): void {
    const idsAsignados = this.juegosAsignados.map(j => j.id);
    this.juegosDisponibles = this.todosLosJuegos.filter(j => !idsAsignados.includes(j.id));
  }

  agregarJuego(): void {
    if (!this.juegoSeleccionadoId) return;
    
    const juegoId = this.juegoSeleccionadoId;
    const dificultad = this.dificultadSeleccionada;
    
    // Reset selection immediately
    this.juegoSeleccionadoId = null;
    
    this.agendaService.assignJuego(this.agendaId, juegoId, dificultad).subscribe({
      next: () => {
        this.cargarDatos();
      },
      error: (err) => {
        if (err.status === 409) {
          Swal.fire('Juego Duplicado', 'Este juego ya está en la agenda', 'warning');
        } else {
          console.log(err);
        }
        this.juegoSeleccionadoId = juegoId;
      }
    });
  }

  quitarJuego(juegoId: number): void {
    this.agendaService.removeJuegoFromAgenda(this.agendaId, juegoId).subscribe({
      next: () => {
        // Reload data from server
        this.cargarDatos();
      },
      error: (err) => Swal.fire('Error', 'No se pudo quitar el juego.', 'error')
    });
  }
  
  volver(): void {
    this.router.navigate(['/agendas']);
  }

  getDificultadTexto(dificultad: number): string {
    switch (dificultad) {
      case 1: return 'Fácil';
      case 2: return 'Medio';
      case 3: return 'Difícil';
      default: return '';
    }
  }

  getDificultadClass(dificultad: number): string {
    switch (dificultad) {
      case 1: return 'bg-success';
      case 2: return 'bg-warning';
      case 3: return 'bg-danger';
      default: return 'bg-secondary';
    }
  }
}