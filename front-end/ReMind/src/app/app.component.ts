import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { AuthComponent } from './pages/auth/auth.component';
import { LadingPageComponent } from './pages/lading-page/lading-page.component';
@Component({
  selector: 'app-root',
  imports: [RouterOutlet, AuthComponent, LadingPageComponent],
  standalone: true,
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  title = 'ReMind';
}
