// src/app/pages/home/home.component.ts

import { Component, ViewChild } from '@angular/core';
import { NavComponent } from '../../components/nav/nav.component';
import { DirectAccessComponent } from '../../components/direct-access/direct-access.component';
import { HeaderComponent } from '../../components/header/header.component';
import { ButtonModule } from 'primeng/button';
import { CommonModule } from '@angular/common';
import { JuegoCambioComponent } from '../../components/juego-cambio/juego-cambio.component';
import { ImageSequenceGameComponent } from '../../components/image-sequence-game/image-sequence-game.component';
import { MatchingGameComponent } from '../../components/matching-game/matching-game.component'; 
import { ClothingGameComponent } from '../../components/clothing-game/clothing-game.component';
import { EuroDenominationComponent } from "../../components/euro-denomination-component/euro-denomination-component.component";

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
    
]
})
export class HomeComponent {

  

}