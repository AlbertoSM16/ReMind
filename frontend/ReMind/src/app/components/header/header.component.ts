import { Component } from '@angular/core';
import { AvatarModule } from 'primeng/avatar';
import { AuthService } from '../../services/auth.service';
@Component({
  selector: 'header-component',
  imports: [AvatarModule],
  templateUrl: './header.component.html',
  styleUrl: './header.component.css',
})
export class HeaderComponent {
  constructor(private authService: AuthService) {}

  logout(): void {
    this.authService.logout();
  }
}
