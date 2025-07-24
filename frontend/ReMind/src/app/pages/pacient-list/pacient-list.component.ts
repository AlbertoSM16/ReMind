import { Component, OnInit } from '@angular/core';
import { HeaderComponent } from "../../components/header/header.component";
import { NavComponent } from "../../components/nav/nav.component";
import { Router } from '@angular/router';

@Component({
  selector: 'app-pacient-list',
  imports: [HeaderComponent, NavComponent],
  templateUrl: './pacient-list.component.html',
  styleUrl: './pacient-list.component.css'
})
export class PacientListComponent implements OnInit {

  constructor(private router: Router) { }

  ngOnInit(): void {
  }

  goToCreatePatient(): void {
    this.router.navigate(['/create-patient']);
  
  }
}
