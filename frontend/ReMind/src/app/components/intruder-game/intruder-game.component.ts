import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';

interface WordSet {
  options: { word: string; isIntruder: boolean }[];
  explanation: string; 
}

@Component({
  selector: 'app-intruder-game',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './intruder-game.component.html',
  styleUrls: ['./intruder-game.component.css']
})
export class IntruderGameComponent implements OnInit {

  private allSets: WordSet[] = [
    {
      options: [{ word: 'Manzana', isIntruder: false }, { word: 'Pera', isIntruder: false }, { word: 'Coche', isIntruder: true }],
      explanation: '"Manzana" y "Pera" son frutas.'
    },
    {
      options: [{ word: 'Rojo', isIntruder: false }, { word: 'Lápiz', isIntruder: true }, { word: 'Azul', isIntruder: false }],
      explanation: '"Rojo" y "Azul" son colores.'
    },
    {
      options: [{ word: 'Perro', isIntruder: false }, { word: 'Gato', isIntruder: false }, { word: 'Mesa', isIntruder: true }],
      explanation: '"Perro" y "Gato" son animales.'
    },
    {
      options: [{ word: 'Correr', isIntruder: false }, { word: 'Nadar', isIntruder: false }, { word: 'Libro', isIntruder: true }],
      explanation: '"Correr" y "Nadar" son acciones (verbos).'
    },
    {
      options: [{ word: 'Sol', isIntruder: true }, { word: 'Río', isIntruder: false }, { word: 'Mar', isIntruder: false }],
      explanation: '"Río" y "Mar" son masas de agua.'
    }
  ];

  currentSet: WordSet | null = null;
  shuffledOptions: { word: string; isIntruder: boolean }[] = [];
  selectedOption: { word: string; isIntruder: boolean } | null = null;
  feedbackMessage = '';
  isCorrect: boolean | null = null;
  score = 0;
  gameOver = false;
  private currentRound = 0;

  constructor() { }

  ngOnInit(): void {
    this.startGame();
  }

  startGame(): void {
    this.currentRound = 0;
    this.score = 0;
    this.gameOver = false;
    this.allSets = this.shuffleArray(this.allSets); // Baraja las rondas
    this.loadNextRound();
  }

  loadNextRound(): void {
    if (this.currentRound < this.allSets.length) {
      this.currentSet = this.allSets[this.currentRound];
      this.shuffledOptions = this.shuffleArray([...this.currentSet.options]); // Baraja las opciones de la ronda actual
      this.selectedOption = null;
      this.isCorrect = null;
      this.feedbackMessage = '';
      this.currentRound++;
    } else {
      this.gameOver = true;
      this.feedbackMessage = `¡Juego terminado! Tu puntuación final es: ${this.score} de ${this.allSets.length}.`;
    }
  }

  selectOption(option: { word: string; isIntruder: boolean }): void {
    if (this.selectedOption) return; // Si ya se eligió una opción, no hacer nada

    this.selectedOption = option;
    if (option.isIntruder) {
      this.isCorrect = true;
      this.feedbackMessage = `¡Correcto! ${option.word} es la intrusa. ${this.currentSet?.explanation}`;
      this.score++;
    } else {
      this.isCorrect = false;
      const intruder = this.currentSet?.options.find(o => o.isIntruder)?.word;
      this.feedbackMessage = `Incorrecto. La palabra intrusa era "${intruder}". ${this.currentSet?.explanation}`;
    }
  }

  // Función genérica para barajar un array
  private shuffleArray(array: any[]): any[] {
    for (let i = array.length - 1; i > 0; i--) {
      const j = Math.floor(Math.random() * (i + 1));
      [array[i], array[j]] = [array[j], array[i]];
    }
    return array;
  }
}