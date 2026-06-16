import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';

// Se añade la propiedad 'audioSrc' para guardar la ruta del sonido
interface Image {
  id: number;
  src: string;
  flipped: boolean;
  audioSrc: string; 
}

@Component({
  selector: 'app-image-sequence-game',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './image-sequence-game.component.html',
  styleUrls: ['./image-sequence-game.component.css']
})
export class ImageSequenceGameComponent implements OnInit {
  @Input() dificultad: number = 1;
  @Output() gameCompleted = new EventEmitter<void>();

  title = 'Juego de Secuencia de Imágenes';
  
  // AÑADIDO: Se agrega la ruta del audio a cada imagen
  images: Image[] = [
    { id: 1, src: 'assets/gameSequence/image1.png', flipped: false, audioSrc: 'assets/gameSequence/audio/coche.mp3' },
    { id: 2, src: 'assets/gameSequence/image2.png', flipped: false, audioSrc: 'assets/gameSequence/audio/moto.mp3' },
    { id: 3, src: 'assets/gameSequence/image3.png', flipped: false, audioSrc: 'assets/gameSequence/audio/avion.mp3' },
    { id: 4, src: 'assets/gameSequence/image4.png', flipped: false, audioSrc: 'assets/gameSequence/audio/helicoptero.mp3' },
    { id: 5, src: 'assets/gameSequence/image5.png', flipped: false, audioSrc: 'assets/gameSequence/audio/barco.mp3' },
    { id: 6, src: 'assets/gameSequence/image6.png', flipped: false, audioSrc: 'assets/gameSequence/audio/bus.mp3' },
  ];

  sequenceLength: number = 4;
  displayDuration: number = 2500; 
  currentSequence: Image[] = [];
  userSequence: number[] = [];
  message: string = '';
  gameStarted: boolean = false;
  showAllImages: boolean = false;

  constructor() {}

  ngOnInit(): void {
    switch (this.dificultad) {
      case 1:
        this.sequenceLength = 4;
        break;
      case 2:
        this.sequenceLength = 5;
        break;
      case 3:
        this.sequenceLength = 6;
        break;
      default:
        this.sequenceLength = 4;
    }
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

    const shuffledImages = this.shuffleArray([...this.images]);
    this.currentSequence = shuffledImages.slice(0, this.sequenceLength);

    let i = 0;
    const interval = setInterval(() => {
      if (i < this.currentSequence.length) {
        const imgToFlip = this.images.find(img => img.id === this.currentSequence[i].id);
        if (imgToFlip) {
          imgToFlip.flipped = false;
          this.playAudio(imgToFlip.audioSrc);
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
      return;
    }

    const selectedImg = this.images.find(img => img.id === imageId);
    if (selectedImg) {
      if (this.userSequence.length < this.sequenceLength && !this.userSequence.includes(imageId)) {
        this.userSequence.push(imageId);
        selectedImg.flipped = false; 
      }
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
      this.gameCompleted.emit();
    } else {
      const correctOrderIds = this.currentSequence.map(img => img.id);
      this.message = `¡Incorrecto! La secuencia correcta era: ${correctOrderIds.join(', ')}. 😔`;
    }

    this.currentSequence.forEach(imgInSeq => {
      const originalImg = this.images.find(img => img.id === imgInSeq.id);
      if (originalImg) {
        originalImg.flipped = false;
      }
    });

    setTimeout(() => {
        this.images.forEach(img => img.flipped = true);
    }, 3000);
  }

  private shuffleArray(array: any[]): any[] {
    for (let i = array.length - 1; i > 0; i--) {
      const j = Math.floor(Math.random() * (i + 1));
      [array[i], array[j]] = [array[j], array[i]];
    }
    return array;
  }

  playAudio(audioSrc: string): void {
    if (audioSrc) {
      const audio = new Audio(audioSrc);
      audio.play();
    }
  }
}