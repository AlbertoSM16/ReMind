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
    { id: 1, src: 'assets/img/game/image1.png', flipped: false },
    { id: 2, src: 'assets/img/game/image2.png', flipped: false },
    { id: 3, src: 'assets/img/game/image3.png', flipped: false },
    { id: 4, src: 'assets/img/game/image4.png', flipped: false },
    { id: 5, src: 'assets/img/game/image5.png', flipped: false },
    { id: 6, src: 'assets/img/game/image6.png', flipped: false }
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
    this.images.forEach(img => img.flipped = true); // All images face down
  }

  startGame(): void {
    this.resetGame();
    this.gameStarted = true;
    this.message = 'Memoriza la secuencia...';

    // Shuffle images and pick a subset for the sequence
    const shuffledImages = [...this.images].sort(() => 0.5 - Math.random());
    this.currentSequence = shuffledImages.slice(0, this.sequenceLength);

    // Temporarily show the sequence
    let i = 0;
    const interval = setInterval(() => {
      if (i < this.currentSequence.length) {
        // Find the image in the main `images` array and flip it
        const imgToFlip = this.images.find(img => img.id === this.currentSequence[i].id);
        if (imgToFlip) {
          imgToFlip.flipped = false; // Show image
        }
        i++;
      } else {
        clearInterval(interval);
        // After displayDuration, flip them back face down
        setTimeout(() => {
          this.images.forEach(img => img.flipped = true); // All images face down again
          this.message = 'Ahora, haz clic en las imágenes en el orden correcto.';
          this.showAllImages = true; // Allow user to click on all images
        }, this.displayDuration);
      }
    }, this.displayDuration);
  }

  selectImage(imageId: number): void {
    if (!this.gameStarted || !this.showAllImages) {
      return; // Game not in active selection phase
    }

    this.userSequence.push(imageId);

    // Temporarily flip the selected image to show it
    const selectedImg = this.images.find(img => img.id === imageId);
    if (selectedImg) {
      selectedImg.flipped = false;
    }

    if (this.userSequence.length === this.sequenceLength) {
      this.checkSequence();
      this.gameStarted = false; // End of current round
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

    // Reveal the correct sequence temporarily for review
    this.currentSequence.forEach(imgInSeq => {
      const originalImg = this.images.find(img => img.id === imgInSeq.id);
      if (originalImg) {
        originalImg.flipped = false; // Show correct images
      }
    });

    setTimeout(() => {
        this.images.forEach(img => img.flipped = true); // Flip all back down
    }, 3000); // Keep result visible for a bit
  }
}