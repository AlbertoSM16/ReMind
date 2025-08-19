import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'lading-page',
  imports: [],
  templateUrl: './landing-page.component.html',
  styleUrls: ['./landing-page.component.css']
})
export class LadingPageComponent {
  linea1: string = 'Una rutina para la mente, un';
  linea2: string = 'apoyo para';

  constructor(private router:Router) { }

  ngOnInit(): void {
  }
  

  toLogin(){
    this.router.navigate(['/login']);
  }
}
