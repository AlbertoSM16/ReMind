import { Component, OnInit, Input } from '@angular/core'; // Importa Input
import { CommonModule } from '@angular/common';
import { EuroDenominationComponent } from '../euro-denomination-component/euro-denomination-component.component';

interface Denomination {
  value: number;
  imageUrl: string;
}

@Component({
  selector: 'app-juego-cambio',
  standalone: true,
  imports: [
    CommonModule,
    EuroDenominationComponent
  ],
  templateUrl: './juego-cambio.component.html',
  styleUrls: ['./juego-cambio.component.css']
})
export class JuegoCambioComponent implements OnInit {
  
  // AÑADIDO: Recibe la dificultad como un parámetro de entrada
  @Input() dificultad: number = 1;

  title = 'Juego de Cambio de Euro';
  amountToMatch: number = 0; // Para dificultad 1 y 2: el total a devolver. Para dificultad 3: el cambio a calcular.
  currentAmount: number = 0;
  message: string = '';
  
  // AÑADIDO: Variables para la dificultad 3
  precioArticulo: number = 0;
  dineroEntregado: number = 0;

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

  startNewRound() {
    this.currentAmount = 0;
    this.message = '';

    if (this.dificultad === 3) {
      // Dificultad 3: Calcular el cambio
      this.precioArticulo = parseFloat((Math.random() * (15 - 1) + 1).toFixed(2));
      const dineroExtra = parseFloat((Math.random() * (10 - 1) + 1).toFixed(2));
      this.dineroEntregado = this.precioArticulo + dineroExtra;
      this.amountToMatch = parseFloat((this.dineroEntregado - this.precioArticulo).toFixed(2));
      
    } else {
      // Dificultad 1 y 2: Devolver una cantidad fija
      this.amountToMatch = parseFloat((Math.random() * (20 - 0.50) + 0.50).toFixed(2));
    }
  }

  addAmount(value: number) {
    this.currentAmount = parseFloat((this.currentAmount + value).toFixed(2));
    this.checkAmount();
  }
  
  // MODIFICADO: Lógica de comprobación separada
  checkAmount() {
    if (this.currentAmount === this.amountToMatch) {
      this.message = '¡Felicidades! Has devuelto la cantidad exacta. 🎉';
    } else if (this.currentAmount > this.amountToMatch) {
      this.message = '¡Te has pasado! Vuelve a intentarlo. 😔';
    } else {
      // Lógica condicional para el mensaje
      if (this.dificultad === 1) {
        // Dificultad 1: Muestra cuánto falta
        this.message = `Te faltan ${(this.amountToMatch - this.currentAmount).toFixed(2)} €`;
      } else {
        // Dificultad 2 y 3: Mensaje genérico
        this.message = 'Sigue añadiendo dinero...';
      }
    }
  }

  resetGame() {
    this.startNewRound();
  }
}