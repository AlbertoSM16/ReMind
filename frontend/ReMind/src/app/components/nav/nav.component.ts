import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { DrawerModule } from 'primeng/drawer';
import { ButtonModule } from 'primeng/button';
import { RouterModule } from '@angular/router';
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
  
  constructor(private authService: AuthService) {}
  
  ngOnInit(): void {
    this.userRole = this.authService.getRole();
  }
}