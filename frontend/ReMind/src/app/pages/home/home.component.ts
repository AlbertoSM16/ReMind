// src/app/pages/home/home.component.ts

import { Component, OnInit, ViewChild } from '@angular/core';
import { NavComponent } from '../../components/nav/nav.component';
import { DirectAccessComponent } from '../../components/direct-access/direct-access.component';
import { HeaderComponent } from '../../components/header/header.component';
import { ButtonModule } from 'primeng/button';
import { CommonModule } from '@angular/common';

import { AuthService } from '../../services/auth.service';
import { MatchingGameComponent } from "../../components/matching-game/matching-game.component";
import { JuegoCambioComponent } from "../../components/juego-cambio/juego-cambio.component";

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
    MatchingGameComponent,
    JuegoCambioComponent
]
})
export class HomeComponent implements OnInit {

  userRole: string | null = null;

  constructor(private authService: AuthService) {}

  ngOnInit(): void {
    this.userRole = this.authService.getRole();
  }
}


