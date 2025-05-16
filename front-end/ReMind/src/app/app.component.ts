import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { AuthComponent } from './pages/auth/auth.component';
import { LadingPageComponent } from './pages/lading-page/lading-page.component';
import { HomeComponent } from './pages/home/home.component';
@Component({
  selector: 'app-root',
  imports: [RouterOutlet, AuthComponent, LadingPageComponent, HomeComponent],
  standalone: true,
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  title = 'ReMind';
}
