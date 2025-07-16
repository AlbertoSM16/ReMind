import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common'; // Importa CommonModule para el CurrencyPipe
import { EuroDenominationComponent } from '../euro-denomination-component/euro-denomination-component.component'; // Importa el componente de denominación

interface Denomination {
  value: number;
  imageUrl: string;
}

@Component({
  selector: 'app-juego-cambio',
  standalone: true, // Es un componente standalone
  imports: [
    CommonModule, // Necesario para pipes como 'currency' y directivas como NgClass, NgFor
    EuroDenominationComponent 
  ],
  templateUrl: './juego-cambio.component.html',
  styleUrl: './juego-cambio.component.css'
})
export class JuegoCambioComponent implements OnInit {
  title = 'Juego de Cambio de Euro';
  amountToMatch: number = 0;
  currentAmount: number = 0;
  message: string = '';

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
    { value: 100, imageUrl: 'assets/euros/100-euro-bill.png' },
    { value: 200, imageUrl: 'assets/euros/200-euro-bill.png' },
    { value: 500, imageUrl: 'assets/euros/500-euro-bill.png' },
  ];

  ngOnInit() {
    this.startNewRound();
  }

  startNewRound() {
    this.currentAmount = 0;
    this.message = '';
    this.amountToMatch = parseFloat((Math.random() * (20 - 0.50) + 0.50).toFixed(2));
  }

  addAmount(value: number) {
    this.currentAmount = parseFloat((this.currentAmount + value).toFixed(2));

    if (this.currentAmount === this.amountToMatch) {
      this.message = '¡Felicidades! Has devuelto la cantidad exacta. 🎉';
    } else if (this.currentAmount > this.amountToMatch) {
      this.message = '¡Te has pasado! Vuelve a intentarlo. 😔';
    } else {
      this.message = `Te faltan ${ (this.amountToMatch - this.currentAmount).toFixed(2) } €`;
    }
  }

  resetGame() {
    this.startNewRound();
  }
}