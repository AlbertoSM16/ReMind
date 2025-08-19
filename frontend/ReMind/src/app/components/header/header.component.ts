import { Component } from '@angular/core';
import { AvatarModule } from 'primeng/avatar';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';
@Component({
  selector: 'header-component',
  imports: [AvatarModule],
  templateUrl: './header.component.html',
  styleUrl: './header.component.css',
})
export class HeaderComponent {
  constructor(private authService: AuthService, private router:Router) {}

  logout(): void {
    this.authService.logout();
  }

  goHome(): void{
    this.router.navigate(['/home']);

  }
}
