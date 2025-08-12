
import { Component, OnInit } from '@angular/core';
import { AgendaCardComponent } from '../../components/agenda-card/agenda-card.component';
import { DoctorCardComponent } from '../doctor-card/doctor-card.component';
import { AuthService } from '../../services/auth.service';
import { CommonModule, NgIf } from '@angular/common';
import { Router } from '@angular/router';
import { PacientesCardComponent } from '../pacientes-card/pacientes-card.component';
@Component({
  selector: 'direct-access',
  standalone: true,
  imports: [AgendaCardComponent, DoctorCardComponent,CommonModule, PacientesCardComponent],
  templateUrl: './direct-access.component.html',
  styleUrl: './direct-access.component.css'
})
export class DirectAccessComponent implements OnInit {
  
  userRole: string | null = null;

  constructor(private authService: AuthService, private router: Router) { }

  ngOnInit(): void {
    this.userRole = this.authService.getRole();
    console.log(this.userRole);
  }
  toPacients(){
    this.router.navigate(['/pacients']);
  }
  toAgenda(){
    this.router.navigate(['/agendas']);
  }
  toDoctors(){
    this.router.navigate(['/doctors']);
  }
  
}
