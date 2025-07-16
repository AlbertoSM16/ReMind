import { Component, Input, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common'; // Necesario para el pipe 'currency'

@Component({
  selector: 'app-euro-denomination',
  standalone: true, // ¡Importante!
  imports: [CommonModule], // Importa CommonModule aquí también
  templateUrl: './euro-denomination-component.component.html',
  styleUrls: ['./euro-denomination-component.component.css']
})
export class EuroDenominationComponent {
  @Input() value: number = 0;
  @Input() imageUrl: string = '';

  @Output() clicked = new EventEmitter<number>();

  onClick() {
    this.clicked.emit(this.value);
  }
}