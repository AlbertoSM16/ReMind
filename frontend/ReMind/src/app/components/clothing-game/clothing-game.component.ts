// app/components/clothing-game/clothing-game.component.ts
import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';

interface ClothingItem {
  id: number;
  name: string;
  src: string;
  target: 'head' | 'torso' | 'legs' | 'feet';
  placed: boolean;
}

interface DropTarget {
  id: string;
  item: ClothingItem | null;
}

@Component({
  selector: 'app-clothing-game',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './clothing-game.component.html',
  styleUrls: ['./clothing-game.component.css']
})
export class ClothingGameComponent implements OnInit {

  @Input() dificultad: number = 1; // Añadido para consistencia
  @Output() gameCompleted = new EventEmitter<void>(); // Añadido

  clothingItems: ClothingItem[] = [];
  dropTargets: { [key: string]: DropTarget } = {};
  score = 0;
  gameOver = false;
  feedbackMessage = '';
  private audioInstrucciones: HTMLAudioElement | null = null;
  title = 'Vístete ';

  constructor() { }

  ngOnInit(): void {
    this.initializeGame();
  }

  initializeGame(): void {
    this.clothingItems = [
      { id: 1, name: 'Gorro', src: 'assets/clothing/gorro.png', target: 'head', placed: false },
      { id: 2, name: 'Camiseta', src: 'assets/clothing/camiseta.png', target: 'torso', placed: false },
      { id: 3, name: 'Pantalón', src: 'assets/clothing/pantalon.png', target: 'legs', placed: false },
      { id: 4, name: 'Zapatos', src: 'assets/clothing/zapatos.png', target: 'feet', placed: false },
    ];
    this.clothingItems = this.shuffleArray(this.clothingItems);
    this.dropTargets = {
      head: { id: 'head', item: null },
      torso: { id: 'torso', item: null },
      legs: { id: 'legs', item: null },
      feet: { id: 'feet', item: null },
    };
    this.score = 0;
    this.gameOver = false;
    this.feedbackMessage = '';
  }

  shuffleArray(array: any[]): any[] {
    for (let i = array.length - 1; i > 0; i--) {
      const j = Math.floor(Math.random() * (i + 1));
      [array[i], array[j]] = [array[j], array[i]];
    }
    return array;
  }

  onDragStart(event: DragEvent, item: ClothingItem): void {
    event.dataTransfer?.setData('itemId', item.id.toString());
  }

  onDragOver(event: DragEvent): void {
    event.preventDefault();
  }

  onDrop(event: DragEvent, targetId: 'head' | 'torso' | 'legs' | 'feet'): void {
    event.preventDefault();
    const itemId = event.dataTransfer?.getData('itemId');
    if (itemId) {
      const item = this.clothingItems.find(i => i.id === +itemId);
      const target = this.dropTargets[targetId];

      if (item && !item.placed && !target.item) {
        if (item.target === targetId) {
          item.placed = true;
          target.item = item;
          this.score++;
          this.feedbackMessage = `¡Bien hecho! Has colocado la ${item.name.toLowerCase()}.`;
          this.checkGameOver();
        } else {
          this.feedbackMessage = '¡Ups! Esa prenda no va en esa parte del cuerpo. Inténtalo de nuevo.';
        }
      }
    }
  }

  checkGameOver(): void {
    if (this.score === this.clothingItems.length) {
      this.gameOver = true;
      this.gameCompleted.emit(); 
    }
  }

  resetGame(): void {
    this.initializeGame();
  }

   reproducirInstrucciones(): void {
    if (this.audioInstrucciones && !this.audioInstrucciones.paused) {
      this.audioInstrucciones.pause();
      this.audioInstrucciones.currentTime = 0;
    }

    this.audioInstrucciones = new Audio('assets/clothing/instrucciones.mp3');
    this.audioInstrucciones.play();
  }
}