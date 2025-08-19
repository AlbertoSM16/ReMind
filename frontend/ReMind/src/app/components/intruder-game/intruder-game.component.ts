import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core'; // Se importa Input
import { CommonModule } from '@angular/common';

interface WordSet {
  options: { word: string; isIntruder: boolean }[];
  explanation: string; 
}

@Component({
  selector: 'intruder-game',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './intruder-game.component.html', 
  styleUrls: ['./intruder-game.component.css']
})
export class IntruderGameComponent implements OnInit {

  @Input() dificultad: number = 1;
  @Output() gameCompleted = new EventEmitter<void>();

  private allSets: WordSet[] = [
    {
      options: [
        { word: 'Coche', isIntruder: true }, 
        { word: 'Manzana', isIntruder: false }, 
        { word: 'Pera', isIntruder: false },
        { word: 'Plátano', isIntruder: false },
        { word: 'Uva', isIntruder: false }
      ],
      explanation: '"Manzana", "Pera", "Plátano" y "Uva" son frutas.'
    },
    {
      options: [
        { word: 'Lápiz', isIntruder: true },
        { word: 'Rojo', isIntruder: false },
        { word: 'Azul', isIntruder: false },
        { word: 'Verde', isIntruder: false },
        { word: 'Amarillo', isIntruder: false }
      ],
      explanation: '"Rojo", "Azul", "Verde" y "Amarillo" son colores.'
    },
    {
      options: [
        { word: 'Mesa', isIntruder: true },
        { word: 'Perro', isIntruder: false },
        { word: 'Gato', isIntruder: false },
        { word: 'León', isIntruder: false },
        { word: 'Tigre', isIntruder: false }
      ],
      explanation: '"Perro", "Gato", "León" y "Tigre" son animales.'
    },
    {
      options: [
        { word: 'Libro', isIntruder: true },
        { word: 'Correr', isIntruder: false },
        { word: 'Nadar', isIntruder: false },
        { word: 'Saltar', isIntruder: false },
        { word: 'Caminar', isIntruder: false }
      ],
      explanation: '"Correr", "Nadar", "Saltar" y "Caminar" son acciones (verbos).'
    },
    {
      options: [
        { word: 'Sol', isIntruder: true },
        { word: 'Río', isIntruder: false },
        { word: 'Mar', isIntruder: false },
        { word: 'Lago', isIntruder: false },
        { word: 'Océano', isIntruder: false }
      ],
      explanation: '"Río", "Mar", "Lago" y "Océano" son masas de agua.'
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
    this.allSets = this.shuffleArray(this.allSets);
    this.loadNextRound();
  }

  loadNextRound(): void {
    if (this.currentRound < this.allSets.length) {
      this.currentSet = this.allSets[this.currentRound];
      
      let optionsToShow = 3;
      if (this.dificultad === 2) {
        optionsToShow = 4;
      } else if (this.dificultad === 3) {
        optionsToShow = 5; 
      }

      const intruder = this.currentSet.options.find(o => o.isIntruder)!;
      const nonIntruders = this.currentSet.options.filter(o => !o.isIntruder);
      const selectedNonIntruders = this.shuffleArray(nonIntruders).slice(0, optionsToShow - 1);
      
      this.shuffledOptions = this.shuffleArray([intruder, ...selectedNonIntruders]);
      
      this.selectedOption = null;
      this.isCorrect = null;
      this.feedbackMessage = '';
      this.currentRound++;
    } else {
      this.gameOver = true;
      this.feedbackMessage = `¡Juego terminado! Tu puntuación final es: ${this.score} de ${this.allSets.length}.`;
      this.gameCompleted.emit();
    }
  }

  selectOption(option: { word: string; isIntruder: boolean }): void {
    if (this.selectedOption) return;

    this.selectedOption = option;
    if (option.isIntruder) {
      this.isCorrect = true;
      this.feedbackMessage = `¡Correcto! ${option.word} es la intrusa. ${this.currentSet?.explanation}`;
      this.score++;
    } else {
      this.isCorrect = false;
      const intruderWord = this.currentSet?.options.find(o => o.isIntruder)?.word;
      this.feedbackMessage = `Incorrecto. La palabra intrusa era "${intruderWord}". ${this.currentSet?.explanation}`;
    }
  }

  private shuffleArray(array: any[]): any[] {
    for (let i = array.length - 1; i > 0; i--) {
      const j = Math.floor(Math.random() * (i + 1));
      [array[i], array[j]] = [array[j], array[i]];
    }
    return array;
  }
}