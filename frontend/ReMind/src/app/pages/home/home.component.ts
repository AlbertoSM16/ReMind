// src/app/pages/home/home.component.ts

import { Component, OnInit, ViewChild } from '@angular/core';
import { NavComponent } from '../../components/nav/nav.component';
import { DirectAccessComponent } from '../../components/direct-access/direct-access.component';
import { HeaderComponent } from '../../components/header/header.component';
import { ButtonModule } from 'primeng/button';
import { CommonModule } from '@angular/common';
import { JuegoCambioComponent } from '../../components/juego-cambio/juego-cambio.component';
import { ImageSequenceGameComponent } from '../../components/image-sequence-game/image-sequence-game.component';
import { MatchingGameComponent } from '../../components/matching-game/matching-game.component'; 
import { ClothingGameComponent } from '../../components/clothing-game/clothing-game.component';
import { IntruderGameComponent } from '../../components/intruder-game/intruder-game.component';
import { SentenceCompletionGameComponent } from '../../components/sentence-completion-game/sentence-completion-game.component';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'home-component',
  standalone: true,
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css'],
  imports: [
    HeaderComponent,
    NavComponent,
    DirectAccessComponent,
    ButtonModule,
    CommonModule,
    JuegoCambioComponent,
    ImageSequenceGameComponent,
    MatchingGameComponent,
    ClothingGameComponent,
    IntruderGameComponent,
    SentenceCompletionGameComponent
    
]
})
export class HomeComponent implements OnInit {

  userRole: string | null = null;

  constructor(private authService: AuthService) {}

  ngOnInit(): void {
    this.userRole = this.authService.getRole();
  }
}


