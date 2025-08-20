// app/pages/gameplay/gameplay.component.ts
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { AgendaService } from '../../services/agenda.service';
import Swal from 'sweetalert2';
import { MatchingGameComponent } from '../../components/matching-game/matching-game.component';
import { ClothingGameComponent } from '../../components/clothing-game/clothing-game.component';
import { ImageSequenceGameComponent } from '../../components/image-sequence-game/image-sequence-game.component';
import { IntruderGameComponent } from '../../components/intruder-game/intruder-game.component';
import { SentenceCompletionGameComponent } from '../../components/sentence-completion-game/sentence-completion-game.component';
import { JuegoCambioComponent } from '../../components/juego-cambio/juego-cambio.component'; // <-- Añadido
import { HeaderComponent } from '../../components/header/header.component';

@Component({
  selector: 'gameplay',
  standalone: true,
  imports: [
    CommonModule,
    MatchingGameComponent,
    ClothingGameComponent,
    ImageSequenceGameComponent,
    IntruderGameComponent,
    SentenceCompletionGameComponent,
    JuegoCambioComponent ,
    HeaderComponent
  ],
  templateUrl: './gameplay.component.html',
  styleUrls: ['./gameplay.component.css']
})
export class Gameplay implements OnInit {
  codigoJuego: string | null = null;
  dificultad: number = 1;
  agendaId: number | null = null;
  juegoId: number | null = null;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private agendaService: AgendaService
  ) {}

  ngOnInit(): void {
    this.codigoJuego = this.route.snapshot.paramMap.get('codigo');
    this.dificultad = Number(this.route.snapshot.paramMap.get('dificultad'));
    this.agendaId = Number(this.route.snapshot.paramMap.get('agendaId'));
    this.juegoId = Number(this.route.snapshot.paramMap.get('juegoId'));
  }
  
  onGameComplete(): void {
    if (this.agendaId && this.juegoId) {
      this.agendaService.completarJuego(this.agendaId, this.juegoId).subscribe({
        next: () => {
          Swal.fire({
            title: '¡Felicidades!',
            text: 'Has completado el juego.',
            icon: 'success',
            confirmButtonText: 'Volver a la agenda'
          }).then(() => {
            this.router.navigate(['/paciente-agenda']);
          });
        },
        error: (err) => {
          Swal.fire('Error', 'No se pudo registrar la finalización del juego.', 'error');
        }
      });
    }
  }
}