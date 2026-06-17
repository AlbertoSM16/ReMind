import { Component, Input, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-instrucciones-modal',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './instrucciones-modal.component.html',
  styleUrls: ['./instrucciones-modal.component.css']
})
export class InstruccionesModalComponent {
  @Input() titulo: string = '';
  @Input() texto: string = '';
  @Input() audioSrc: string = '';
  @Input() visible: boolean = false;
  @Output() visibleChange = new EventEmitter<boolean>();

  private audioInstrucciones: HTMLAudioElement | null = null;

  cerrar(): void {
    this.visible = false;
    this.visibleChange.emit(false);
    if (this.audioInstrucciones && !this.audioInstrucciones.paused) {
      this.audioInstrucciones.pause();
      this.audioInstrucciones.currentTime = 0;
    }
  }

  reproducirInstrucciones(): void {
    if (!this.audioSrc) return;
    if (this.audioInstrucciones && !this.audioInstrucciones.paused) {
      this.audioInstrucciones.pause();
      this.audioInstrucciones.currentTime = 0;
    }
    this.audioInstrucciones = new Audio(this.audioSrc);
    this.audioInstrucciones.play();
  }
}
