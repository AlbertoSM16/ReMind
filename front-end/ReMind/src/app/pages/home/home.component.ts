import { Component } from '@angular/core';
import { HeaderComponent } from '../../components/header/header.component';
import { DirectAccessComponent } from "../../components/direct-access/direct-access.component";

@Component({
  selector: 'home-component',
  imports: [HeaderComponent, DirectAccessComponent],
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent {

}
