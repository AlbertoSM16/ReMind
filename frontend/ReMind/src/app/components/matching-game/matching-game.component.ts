import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common'; // Necesario para *ngIf y *ngFor

// Define las interfaces
interface Image {
  id: number;
  src: string;
  pairId: number;
  type: 'item' | 'person';
  name: string;
}

interface DropZone {
  id: number;
  person: Image | null;
  item: Image | null;
  correct: boolean;
}

@Component({
  selector: 'app-matching-game',
  standalone: true, // <-- AÑADE ESTO
  imports: [CommonModule], // <-- AÑADE ESTO
  templateUrl: './matching-game.component.html',
  styleUrls: ['./matching-game.component.css']
})
export class MatchingGameComponent implements OnInit {
  images: Image[] = [];
  unmatchedImages: Image[] = [];
  dropZones: DropZone[] = [];
  gameOver = false;
  successMessage = '';

  ngOnInit(): void {
    this.initializeGame();
  }

  initializeGame(): void {
    const pairs = [
      { id: 1, item: 'assets/matching/llave-inglesa.png', person: 'assets/matching/fontanero.png', itemName: 'Llave Inglesa', personName: 'Fontanero' },
      { id: 2, item: 'assets/matching/pizarra.png', person: 'assets/matching/profesor.png', itemName: 'Pizarra', personName: 'Profesor' },
      { id: 3, item: 'assets/matching/utensilios.png', person: 'assets/matching/cocinero.png', itemName: 'Utiles de cocina', personName: 'Cocinero' },
      { id: 4, item: 'assets/matching/jeringuilla.png', person: 'assets/matching/medica.png', itemName: 'Estetoscopio', personName: 'Médica' }
    ];

    this.images = [];
    pairs.forEach(pair => {
      this.images.push({ id: pair.id * 2 - 1, src: pair.item, pairId: pair.id, type: 'item', name: pair.itemName });
      this.images.push({ id: pair.id * 2, src: pair.person, pairId: pair.id, type: 'person', name: pair.personName });
    });

    this.unmatchedImages = this.shuffleArray([...this.images]);

    this.dropZones = [
      { id: 1, person: null, item: null, correct: false },
      { id: 2, person: null, item: null, correct: false },
      { id: 3, person: null, item: null, correct: false },
      { id: 4, person: null, item: null, correct: false }
    ];
    this.gameOver = false;
    this.successMessage = '';
  }

  shuffleArray(array: any[]): any[] {
    for (let i = array.length - 1; i > 0; i--) {
      const j = Math.floor(Math.random() * (i + 1));
      [array[i], array[j]] = [array[j], array[i]];
    }
    return array;
  }

  onDragStart(event: DragEvent, image: Image): void {
    event.dataTransfer?.setData('imageId', image.id.toString());
  }

  onDragOver(event: DragEvent): void {
    event.preventDefault();
  }

  onDrop(event: DragEvent, dropZone: DropZone): void {
    event.preventDefault();
    const imageId = event.dataTransfer?.getData('imageId');
    if (imageId) {
      const draggedImage = this.images.find(img => img.id === parseInt(imageId, 10));
      if (draggedImage && !this.isImageInDropZone(draggedImage)) {

        if (draggedImage.type === 'person' && !dropZone.person) {
          dropZone.person = draggedImage;
        } else if (draggedImage.type === 'item' && !dropZone.item) {
          dropZone.item = draggedImage;
        }

        this.checkDropZone(dropZone);
        this.checkGameOver();
      }
    }
  }

  isImageInDropZone(image: Image): boolean {
    return this.dropZones.some(dz => (dz.person && dz.person.id === image.id) || (dz.item && dz.item.id === image.id));
  }

  getUnmatchedImages(): Image[] {
    return this.unmatchedImages.filter(img => !this.isImageInDropZone(img));
  }


  checkDropZone(dropZone: DropZone): void {
    if (dropZone.person && dropZone.item) {
      if (dropZone.person.pairId === dropZone.item.pairId) {
        dropZone.correct = true;
      }
    }
  }

  checkGameOver(): void {
    const allCorrect = this.dropZones.every(dz => dz.correct);
    if (allCorrect) {
      this.gameOver = true;
      this.successMessage = '¡Felicidades! Has unido todas las parejas correctamente.';
    }
  }

  resetGame(): void {
    this.initializeGame();
  }
}