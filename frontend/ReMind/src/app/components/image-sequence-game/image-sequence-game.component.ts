import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-image-sequence-game',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './image-sequence-game.component.html',
  styleUrls: ['./image-sequence-game.component.css']
})
export class ImageSequenceGameComponent implements OnInit {
  title = 'Juego de Secuencia de Imágenes';
  images = [
    { id: 1, src: 'assets/gameSequence/image1.png', flipped: false },
    { id: 2, src: 'assets/gameSequence/image2.png', flipped: false },
    { id: 3, src: 'assets/gameSequence/image3.png', flipped: false },
    { id: 4, src: 'assets/gameSequence/image4.png', flipped: false },
    { id: 5, src: 'assets/gameSequence/image5.png', flipped: false },
    { id: 6, src: 'assets/gameSequence/image6.png', flipped: false }
  ];
  sequenceLength = 3; // Number of images in the sequence
  displayDuration = 1500; // milliseconds
  currentSequence: { id: number; src: string; flipped: boolean }[] = [];
  userSequence: number[] = [];
  message: string = '';
  gameStarted: boolean = false;
  showAllImages: boolean = false; // To show all images initially for selection

  constructor() {}

  ngOnInit(): void {
    this.resetGame();
  }

  resetGame(): void {
    this.gameStarted = false;
    this.showAllImages = false;
    this.currentSequence = [];
    this.userSequence = [];
    this.message = 'Haz clic en "Empezar" para ver la secuencia.';
    this.images.forEach(img => img.flipped = true);
  }

  startGame(): void {
    this.resetGame();
    this.gameStarted = true;
    this.message = 'Memoriza la secuencia...';

    const shuffledImages = [...this.images].sort(() => 0.5 - Math.random());
    this.currentSequence = shuffledImages.slice(0, this.sequenceLength);

    // Temporarily show the sequence
    let i = 0;
    const interval = setInterval(() => {
      if (i < this.currentSequence.length) {
        const imgToFlip = this.images.find(img => img.id === this.currentSequence[i].id);
        if (imgToFlip) {
          imgToFlip.flipped = false; 
        }
        i++;
      } else {
        clearInterval(interval);
        setTimeout(() => {
          this.images.forEach(img => img.flipped = true); 
          this.message = 'Ahora, haz clic en las imágenes en el orden correcto.';
          this.showAllImages = true; 
        }, this.displayDuration);
      }
    }, this.displayDuration);
  }

  selectImage(imageId: number): void {
    if (!this.gameStarted || !this.showAllImages) {
      return; // Game not in active selection phase
    }

    this.userSequence.push(imageId);

    const selectedImg = this.images.find(img => img.id === imageId);
    if (selectedImg) {
      selectedImg.flipped = false;
    }

    if (this.userSequence.length === this.sequenceLength) {
      this.checkSequence();
      this.gameStarted = false; 
    } else {
      this.message = `Seleccionada ${this.userSequence.length} de ${this.sequenceLength} imágenes.`;
    }
  }

  checkSequence(): void {
    let correct = true;
    for (let i = 0; i < this.sequenceLength; i++) {
      if (this.userSequence[i] !== this.currentSequence[i].id) {
        correct = false;
        break;
      }
    }

    if (correct) {
      this.message = '¡Felicidades! Secuencia correcta. 🎉';
    } else {
      const correctOrderIds = this.currentSequence.map(img => img.id);
      this.message = `¡Incorrecto! La secuencia correcta era: ${correctOrderIds.join(', ')}. 😔`;
    }

    this.currentSequence.forEach(imgInSeq => {
      const originalImg = this.images.find(img => img.id === imgInSeq.id);
      if (originalImg) {
        originalImg.flipped = false; // Show correct images
      }
    });

    setTimeout(() => {
        this.images.forEach(img => img.flipped = true); 
    }, 3000); 
  }
}