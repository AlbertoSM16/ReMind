import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';
import { EuroDenominationComponent } from '../euro-denomination-component/euro-denomination-component.component';
import { InstruccionesModalComponent } from '../instrucciones-modal/instrucciones-modal.component';

interface Denomination {
  value: number;
  imageUrl: string;
}

@Component({
  selector: 'app-juego-cambio',
  standalone: true,
  imports: [
    CommonModule,
    EuroDenominationComponent,
    InstruccionesModalComponent
  ],
  templateUrl: './juego-cambio.component.html',
  styleUrls: ['./juego-cambio.component.css']
})
export class JuegoCambioComponent implements OnInit {

  @Input() dificultad: number = 1;
  @Output() gameCompleted = new EventEmitter<void>();

  title = 'Pagos exactos (El cambio)';
  amountToMatch: number = 0;
  currentAmount: number = 0;
  message: string = '';

  precioArticulo: number = 0;
  dineroEntregado: number = 0;

  modalVisible = false;

  euroDenominations: Denomination[] = [
    { value: 0.01, imageUrl: 'assets/euros/1-cent.png' },
    { value: 0.02, imageUrl: 'assets/euros/2-cent.png' },
    { value: 0.05, imageUrl: 'assets/euros/5-cent.png' },
    { value: 0.10, imageUrl: 'assets/euros/10-cent.png' },
    { value: 0.20, imageUrl: 'assets/euros/20-cent.png' },
    { value: 0.50, imageUrl: 'assets/euros/50-cent.png' },
    { value: 1, imageUrl: 'assets/euros/1-euro.png' },
    { value: 2, imageUrl: 'assets/euros/2-euro.png' },
    { value: 5, imageUrl: 'assets/euros/5-euro-bill.png' },
    { value: 10, imageUrl: 'assets/euros/10-euro-bill.png' },
    { value: 20, imageUrl: 'assets/euros/20-euro-bill.png' },
    { value: 50, imageUrl: 'assets/euros/50-euro-bill.png' },
  ];

  ngOnInit() {
    this.startNewRound();
  }

  get modalTexto(): string {
    if (this.dificultad === 1) {
      return 'Imagina que estamos en el supermercado comprando algo. El juego te mostrará el precio del producto y tu misión es añadir dinero hasta que coincida con ese precio. ¡Así compruebas que el cambio es correcto!';
    }
    return 'Imagina que estamos en el supermercado comprando algo. El juego te mostrará el precio del producto y el dinero que has entregado para pagar. Tu misión es calcular el cambio y juntar las vueltas exactas que te tienen que devolver.';
  }

  startNewRound() {
    this.currentAmount = 0;
    this.message = '';

    if (this.dificultad === 1) {
      this.amountToMatch = parseFloat((Math.random() * (20 - 0.50) + 0.50).toFixed(2));
    } else {
      const maxPrecio = this.dificultad === 2 ? 10 : 15;
      const maxExtra = this.dificultad === 2 ? 5 : 10;
      this.precioArticulo = parseFloat((Math.random() * (maxPrecio - 1) + 1).toFixed(2));
      const dineroExtra = parseFloat((Math.random() * (maxExtra - 1) + 1).toFixed(2));
      this.dineroEntregado = this.precioArticulo + dineroExtra;
      this.amountToMatch = parseFloat((this.dineroEntregado - this.precioArticulo).toFixed(2));
    }
  }

  addAmount(value: number) {
    this.currentAmount = parseFloat((this.currentAmount + value).toFixed(2));
    this.checkAmount();
  }

  checkAmount() {
    if (this.currentAmount === this.amountToMatch) {
      this.message = '¡Felicidades! Has devuelto la cantidad exacta.';
      this.gameCompleted.emit();
    } else if (this.currentAmount > this.amountToMatch) {
      this.message = '¡Te has pasado! Vuelve a intentarlo.';
    } else {
      if (this.dificultad === 1) {
        this.message = `Te faltan ${(this.amountToMatch - this.currentAmount).toFixed(2)} €`;
      } else {
        this.message = 'Sigue añadiendo dinero...';
      }
    }
  }

  resetGame() {
    this.startNewRound();
  }

  mostrarInstrucciones(): void {
    this.modalVisible = true;
  }
}
