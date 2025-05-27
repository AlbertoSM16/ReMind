import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { DrawerModule } from 'primeng/drawer';
import { ButtonModule } from 'primeng/button';

@Component({
  selector: 'nav-component',
  standalone: true,
  templateUrl: './nav.component.html',
  styleUrls: ['./nav.component.css'],
  imports: [CommonModule, DrawerModule, ButtonModule]
})
export class NavComponent {
  visible = false;

  openDrawer() {
    this.visible = true;
  }

  closeCallback(event: MouseEvent) {
  this.visible = false;
}
}
