import { Component, ViewChild } from '@angular/core';
import { NavComponent } from '../../components/nav/nav.component';
import { DirectAccessComponent } from '../../components/direct-access/direct-access.component';
import { HeaderComponent } from '../../components/header/header.component';
import { ButtonModule } from 'primeng/button';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'home-component',
  standalone: true,
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css'],
  imports: [HeaderComponent, NavComponent, DirectAccessComponent,ButtonModule,CommonModule]
})
export class HomeComponent {
  @ViewChild(NavComponent) nav!: NavComponent;

  openSidebar() {
    this.nav.openDrawer();
  }
}
