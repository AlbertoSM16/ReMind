import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';

// Interfaz para definir la estructura de cada ronda
interface SentenceRound {
  start: string; // Parte inicial de la oración
  end: string;   // Parte final de la oración
  options: {
    word: string;
    isCorrect: boolean;
  }[];
}

@Component({
  selector: 'app-sentence-completion-game',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './sentence-completion-game.component.html',
  styleUrls: ['./sentence-completion-game.component.css']
})
export class SentenceCompletionGameComponent implements OnInit {

  private allRounds: SentenceRound[] = [
    {
      start: 'El perro',
      options: [{ word: 'ladra', isCorrect: true }, { word: 'vuela', isCorrect: false }],
      end: 'muy fuerte por la noche.'
    },
    {
      start: 'Para el desayuno, me gusta beber un vaso de',
      options: [{ word: 'leche', isCorrect: true }, { word: 'piedra', isCorrect: false }],
      end: '.'
    },
    {
      start: 'El sol brilla durante el',
      options: [{ word: 'día', isCorrect: true }, { word: 'lago', isCorrect: false }],
      end: '.'
    },
    {
      start: 'Los pájaros construyen sus',
      options: [{ word: 'nidos', isCorrect: true }, { word: 'coches', isCorrect: false }],
      end: 'en los árboles.'
    },
    {
      start: 'En invierno hace mucho',
      options: [{ word: 'frío', isCorrect: true }, { word: 'calor', isCorrect: false }],
      end: '.'
    }
  ];

  currentRoundData: SentenceRound | null = null;
  shuffledOptions: { word: string; isCorrect: boolean }[] = [];
  selectedOption: { word: string; isCorrect: boolean } | null = null;
  feedbackMessage = '';
  isAnswerCorrect: boolean | null = null;
  score = 0;
  gameOver = false;
  private roundIndex = 0;

  constructor() { }

  ngOnInit(): void {
    this.startGame();
  }

  startGame(): void {
    this.roundIndex = 0;
    this.score = 0;
    this.gameOver = false;
    this.allRounds = this.shuffleArray(this.allRounds); 
    this.loadNextRound();
  }

  loadNextRound(): void {
    if (this.roundIndex < this.allRounds.length) {
      this.currentRoundData = this.allRounds[this.roundIndex];
      this.shuffledOptions = this.shuffleArray([...this.currentRoundData.options]);
      this.selectedOption = null;
      this.isAnswerCorrect = null;
      this.feedbackMessage = '';
      this.roundIndex++;
    } else {
      this.gameOver = true;
      this.feedbackMessage = `¡Juego terminado! Has acertado ${this.score} de ${this.allRounds.length} oraciones.`;
    }
  }

  selectOption(option: { word: string; isCorrect: boolean }): void {
    if (this.selectedOption) return; 

    this.selectedOption = option;
    this.isAnswerCorrect = option.isCorrect;

    if (option.isCorrect) {
      this.feedbackMessage = '¡Muy bien! Esa es la palabra correcta.';
      this.score++;
    } else {
      const correctWord = this.currentRoundData?.options.find(o => o.isCorrect)?.word;
      this.feedbackMessage = `Casi... La palabra correcta era "${correctWord}".`;
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