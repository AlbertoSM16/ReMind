import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { NavComponent } from '../../components/nav/nav.component';
import { DirectAccessComponent } from '../../components/direct-access/direct-access.component';
import { HeaderComponent } from '../../components/header/header.component';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../services/auth.service';
import { AgendaService } from '../../services/agenda.service';

export interface Logro {
  titulo: string;
  icono: string;
  descripcion: string;
  tipo: 'juego' | 'bronce' | 'plata' | 'oro';
  bloqueado?: boolean;
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
    CommonModule
  ]
})
export class HomeComponent implements OnInit {
  userRole: string | null = null;
  userName: string | null = null;
  logrosJuegos: Logro[] = [];
  logrosEspecialesLista: Logro[] = [];
  completadosCount = 0;
  totalJuegos = 0;

  private iconoPorJuego: { [key: string]: string } = {
    'matching-game': 'pi pi-th-large',
    'clothing-game': 'pi pi-user',
    'image-sequence-game': 'pi pi-sort-alt',
    'intruder-game': 'pi pi-search',
    'sentence-complete': 'pi pi-align-left',
    'juego-cambio': 'pi pi-euro'
  };

  private logrosEspeciales = [
    { cantidad: 3, titulo: 'Jugador Bronce', icono: 'pi pi-star', descripcion: '¡Completaste 3 juegos diferentes!', tipo: 'bronce' as const },
    { cantidad: 5, titulo: 'Jugador Plata', icono: 'pi pi-star-fill', descripcion: '¡Completaste 5 juegos diferentes!', tipo: 'plata' as const },
    { cantidad: 6, titulo: 'Jugador Oro', icono: 'pi pi-trophy', descripcion: '¡Completaste todos los juegos!', tipo: 'oro' as const }
  ];

  get progreso(): number {
    if (this.totalJuegos === 0) return 0;
    return Math.round((this.completadosCount / this.totalJuegos) * 100);
  }

  get siguienteLogro(): Logro | null {
    for (const logro of this.logrosEspeciales) {
      if (this.completadosCount < logro.cantidad) {
        return {
          titulo: logro.titulo,
          icono: logro.icono,
          descripcion: `Completa ${logro.cantidad} juegos para conseguirlo`,
          tipo: logro.tipo,
          bloqueado: true
        };
      }
    }
    return null;
  }

  constructor(
    private authService: AuthService,
    private agendaService: AgendaService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.userRole = this.authService.getRole();
    this.userName = sessionStorage.getItem('nombre');

    if (this.userRole === 'paciente') {
      this.cargarJuegosCompletados();
    }
  }

  cargarJuegosCompletados(): void {
    const pacienteId = sessionStorage.getItem('id');
    if (pacienteId) {
      this.agendaService.getJuegosByPacienteId(+pacienteId).subscribe({
        next: (data) => {
          this.totalJuegos = data.juegos?.length || 0;
          const juegosCompletados = data.juegos?.filter((juego: any) => juego.realizado === true) || [];
          this.completadosCount = juegosCompletados.length;

          this.logrosJuegos = juegosCompletados.map((juego: any) => ({
            titulo: juego.nombre,
            icono: this.iconoPorJuego[juego.codigo] || 'pi pi-trophy',
            descripcion: `¡Completaste ${juego.nombre}!`,
            tipo: 'juego' as const
          }));

          this.logrosEspecialesLista = this.logrosEspeciales.map(logro => {
            const desbloqueado = this.completadosCount >= logro.cantidad;
            return {
              titulo: logro.titulo,
              icono: logro.icono,
              descripcion: desbloqueado ? logro.descripcion : `Completa ${logro.cantidad} juegos`,
              tipo: logro.tipo,
              bloqueado: !desbloqueado
            };
          });
        },
        error: () => {
          this.logrosJuegos = [];
          this.logrosEspecialesLista = [];
        }
      });
    }
  }

  goToPacienteAgenda(): void {
    this.router.navigate(['/paciente-agenda']);
  }
}
