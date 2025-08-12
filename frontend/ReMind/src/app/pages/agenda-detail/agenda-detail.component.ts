import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms'; // <-- IMPORTANTE: Añadir FormsModule
import { HeaderComponent } from '../../components/header/header.component';
import { AgendaService } from '../../services/agenda.service';
import { JuegoService } from '../../services/juego.service';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-agenda-detail',
  standalone: true,
  imports: [CommonModule, HeaderComponent, FormsModule], // <-- Añadir FormsModule a los imports
  templateUrl: './agenda-detail.component.html',
  styleUrls: ['./agenda-detail.component.css']
})
export class AgendaDetailComponent implements OnInit {

  agendaId!: number;
  agenda: any;
  juegosAsignados: any[] = [];
  juegosDisponibles: any[] = [];
  todosLosJuegos: any[] = [];

  // Propiedad para vincular con el <select>
  juegoSeleccionadoId: number | null = null;

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
    this.juegoService.getJuegos().subscribe(juegos => {
      this.todosLosJuegos = juegos;
      this.agendaService.getJuegosByAgendaId(this.agendaId).subscribe(asignados => {
        this.juegosAsignados = asignados;
        this.actualizarJuegosDisponibles();
      });
    });
  }

  actualizarJuegosDisponibles(): void {
    const idsAsignados = this.juegosAsignados.map(j => j.id);
    this.juegosDisponibles = this.todosLosJuegos.filter(j => !idsAsignados.includes(j.id));
  }

  // MÉTODO "agregarJuego" ACTUALIZADO
  agregarJuego(): void {
    if (!this.juegoSeleccionadoId) {
      Swal.fire('Atención', 'Por favor, selecciona un juego de la lista.', 'warning');
      return;
    }

    this.agendaService.assignJuego(this.agendaId, this.juegoSeleccionadoId).subscribe({
      next: () => {
        Swal.fire('¡Añadido!', 'El juego se ha asignado a la agenda.', 'success');
        this.juegoSeleccionadoId = null; // Reiniciar el desplegable
        this.cargarDatos(); // Recargar las listas
      },
      error: () => Swal.fire('Error', 'No se pudo añadir el juego.', 'error')
    });
  }

  // El método quitarJuego no necesita cambios
  quitarJuego(juegoId: number): void {
    this.agendaService.removeJuegoFromAgenda(this.agendaId, juegoId).subscribe({
      next: () => {
        Swal.fire('¡Eliminado!', 'El juego se ha quitado de la agenda.', 'success');
        this.cargarDatos();
      },
      error: () => Swal.fire('Error', 'No se pudo quitar el juego.', 'error')
    });
  }
  
  volver(): void {
    this.router.navigate(['/agendas']);
  }
}