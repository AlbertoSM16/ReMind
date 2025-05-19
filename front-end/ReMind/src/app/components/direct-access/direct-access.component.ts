import { Component } from '@angular/core';
import {AgendaCardComponent} from '../../components/agenda-card/agenda-card.component';
import { DoctorCardComponent } from '../doctor-card/doctor-card.component';

@Component({
  selector: 'direct-access',
  standalone: true,
  imports: [AgendaCardComponent, DoctorCardComponent],
  templateUrl: './direct-access.component.html',
  styleUrl: './direct-access.component.css'
})
export class DirectAccessComponent {

}
