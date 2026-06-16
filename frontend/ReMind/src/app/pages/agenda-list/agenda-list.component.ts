import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AgendaService } from '../../services/agenda.service';
import Swal from 'sweetalert2';
import { CommonModule } from '@angular/common';
import { HeaderComponent } from '../../components/header/header.component';

@Component({
  selector: 'app-agenda-list',
  standalone: true,
  imports: [CommonModule, HeaderComponent],
  templateUrl: './agenda-list.component.html',
  styleUrls: ['./agenda-list.component.css']
})
export class AgendaListComponent implements OnInit {

  agendas: any[] = [];
  isLoading = true;

  constructor(
    private agendaService: AgendaService,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.loadAgendas();
  }

  loadAgendas(): void {
    const terapeutaId = sessionStorage.getItem('id');
    if (!terapeutaId) {
      Swal.fire('Error', 'No se pudo identificar al terapeuta. Por favor, inicie sesión de nuevo.', 'error');
      this.router.navigate(['/login']);
      return;
    }

    this.isLoading = true;
    this.agendaService.getAgendasByTerapeuta(+terapeutaId).subscribe({
      next: (data) => {
        this.agendas = data;
        this.isLoading = false;
      },
      error: (error) => {
        this.isLoading = false;
        this.agendas = [];
        if (error.status === 0) {
          Swal.fire('Error de conexión', 'No se puede conectar con el servidor.', 'error');
        } else {
          Swal.fire('Información', 'Asegúrese de tener pacientes a su cargo dados de alta.', 'info');
        }
      }
    });
  }

  // Métodos de ejemplo para los botones
  editAgenda(id: number): void {
    console.log('Editar agenda:', id);
    this.router.navigate(['/edit-agenda', id]);
  }


  viewAgenda(id: number): void {
    this.router.navigate(['/agenda', id]);
  }
}