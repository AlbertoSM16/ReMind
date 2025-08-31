import { Component, OnInit } from '@angular/core';
import { NavComponent } from '../../components/nav/nav.component';
import { DirectAccessComponent } from '../../components/direct-access/direct-access.component';
import { HeaderComponent } from '../../components/header/header.component';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../services/auth.service';
import { AgendaService } from '../../services/agenda.service';
import { TooltipModule } from 'primeng/tooltip';

export interface Logro {
  titulo: string;
  icono: string;
  descripcion: string;
}

@Component({
  selector: 'home-component',
  standalone: true,
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css'],
  imports: [
    HeaderComponent,
    NavComponent,
    DirectAccessComponent,
    CommonModule,
    TooltipModule
  ]
})
export class HomeComponent implements OnInit {
  userRole: string | null = null;
  logrosCompletados: Logro[] = [];

  private iconoPorJuego: { [key: string]: string } = {
    'matching-game': 'bi-puzzle-fill',
    'clothing-game': 'bi-person-check-fill',
    'image-sequence-game': 'bi-images',
    'intruder-game': 'bi-search-heart',
    'sentence-complete': 'bi-chat-left-text-fill',
    'juego-cambio': 'bi-currency-euro'
  };

  private logrosEspeciales = [
    { cantidad: 3, titulo: 'Jugador Bronce', icono: 'bi-award', descripcion: '¡Completaste 3 juegos diferentes!' },
    { cantidad: 5, titulo: 'Jugador Plata', icono: 'bi-award-fill', descripcion: '¡Completaste 5 juegos diferentes!' },
    { cantidad: 6, titulo: 'Jugador Oro', icono: 'bi-trophy-fill', descripcion: '¡Completaste todos los juegos!' }
  ];

  constructor(
    private authService: AuthService,
    private agendaService: AgendaService
  ) {}

  ngOnInit(): void {
    this.userRole = this.authService.getRole();

    if (this.userRole === 'paciente') {
      this.cargarJuegosCompletados();
    }
  }

  cargarJuegosCompletados(): void {
    const pacienteId = sessionStorage.getItem('id');
    if (pacienteId) {
      this.agendaService.getJuegosByPacienteId(+pacienteId).subscribe({
        next: (data) => {
          const juegosCompletados = data.juegos.filter((juego: any) => juego.realizado === true);
          
          const logrosIndividuales = juegosCompletados.map((juego: any) => ({
            titulo: `${juego.nombre}`,
            icono: this.iconoPorJuego[juego.codigo] || 'bi-trophy-fill',
            descripcion: `¡Completaste el juego ${juego.nombre}!`
          }));

          const logrosEspecialesDesbloqueados = this.logrosEspeciales
            .filter(logro => juegosCompletados.length >= logro.cantidad)
            .map(logro => ({
              titulo: logro.titulo,
              icono: logro.icono,
              descripcion: logro.descripcion
            }));

          this.logrosCompletados = [...logrosIndividuales, ...logrosEspecialesDesbloqueados];
        },
        error: (err) => {
          console.error('Error al cargar los juegos del paciente:', err);
          this.logrosCompletados = []; 
        }
      });
    }
  }
}