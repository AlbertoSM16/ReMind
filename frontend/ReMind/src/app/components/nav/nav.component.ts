import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { DrawerModule } from 'primeng/drawer';
import { ButtonModule } from 'primeng/button';
import { Router, RouterModule } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'nav-component',
  standalone: true,
  templateUrl: './nav.component.html',
  styleUrls: ['./nav.component.css'],
  imports: [CommonModule, DrawerModule, ButtonModule, RouterModule]
})
export class NavComponent implements OnInit {
  visible = false;
  userRole: string | null = null;
  userName : string | null = null;

  constructor(private authService: AuthService, private router: Router) { }

  ngOnInit(): void {
    this.userRole = this.authService.getRole();
    this.userName = sessionStorage.getItem('nombre');
  }

  toPacientes(): void {
    this.router.navigate(['/pacients']);
  }
  toAgendas(): void {
    this.router.navigate(['/agendas']);
  }
  toSeguimiento(): void {
    this.router.navigate(['/follow']);
  }
  toTareas(): void {
    this.router.navigate(['/tareas']);
  }
}