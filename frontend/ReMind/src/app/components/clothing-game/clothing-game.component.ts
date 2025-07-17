import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';

// Interfaces para definir la estructura de los datos
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

  clothingItems: ClothingItem[] = [];
  dropTargets: { [key: string]: DropTarget } = {};
  score = 0;
  gameOver = false;
  feedbackMessage = '';

  constructor() { }

  ngOnInit(): void {
    this.initializeGame();
  }

  initializeGame(): void {
    // Definimos las prendas y a qué parte del cuerpo pertenecen
    this.clothingItems = [
      { id: 1, name: 'Gorro', src: 'assets/clothing/gorro.png', target: 'head', placed: false },
      { id: 2, name: 'Camiseta', src: 'assets/clothing/camiseta.png', target: 'torso', placed: false },
      { id: 3, name: 'Pantalón', src: 'assets/clothing/pantalon.png', target: 'legs', placed: false },
      { id: 4, name: 'Zapatos', src: 'assets/clothing/zapatos.png', target: 'feet', placed: false },
    ];

    // Desordenamos las prendas para que aparezcan en un orden aleatorio
    this.clothingItems = this.shuffleArray(this.clothingItems);

    // Inicializamos las zonas del cuerpo donde se puede soltar la ropa
    this.dropTargets = {
      head: { id: 'head', item: null },
      torso: { id: 'torso', item: null },
      legs: { id: 'legs', item: null },
      feet: { id: 'feet', item: null },
    };

    // Reiniciamos el estado del juego
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

  // Eventos de Drag & Drop

  onDragStart(event: DragEvent, item: ClothingItem): void {
    event.dataTransfer?.setData('itemId', item.id.toString());
  }

  onDragOver(event: DragEvent): void {
    event.preventDefault(); // Permite que se pueda soltar el elemento
  }

  onDrop(event: DragEvent, targetId: 'head' | 'torso' | 'legs' | 'feet'): void {
    event.preventDefault();
    const itemId = event.dataTransfer?.getData('itemId');
    if (itemId) {
      const item = this.clothingItems.find(i => i.id === +itemId);
      const target = this.dropTargets[targetId];

      if (item && !item.placed && !target.item) {
        if (item.target === targetId) {
          // Acierto
          item.placed = true;
          target.item = item;
          this.score++;
          this.feedbackMessage = `¡Bien hecho! Has colocado la ${item.name.toLowerCase()}.`;
          this.checkGameOver();
        } else {
          // Fallo
          this.feedbackMessage = '¡Ups! Esa prenda no va en esa parte del cuerpo. Inténtalo de nuevo.';
        }
      }
    }
  }

  checkGameOver(): void {
    if (this.score === this.clothingItems.length) {
      this.gameOver = true;
      this.feedbackMessage = '¡Felicidades! Has vestido completamente la silueta.';
    }
  }

  resetGame(): void {
    this.initializeGame();
  }
}