import { Component } from '@angular/core';
import { Terapeuta } from '../../models/terapeuta.model';
import { TerapeutaService } from '../../services/terapeuta.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-create-terapeuta',
  templateUrl: './create-terapeuta.component.html',
  styleUrls: ['./create-terapeuta.component.css']
})
export class CreateTerapeutaComponent {

  // terapeuta: Terapeuta = {
  //   nombre: '',
  //   correo: '',
  //   contrasena: '',
  //   especialidad: ''
  // };

  // constructor(private terapeutaService: TerapeutaService, private router: Router) { }

  // createTerapeuta(): void {
  //   this.terapeutaService.createTerapeuta(this.terapeuta).subscribe(() => {
  //     this.router.navigate(['/terapeutas']); 
  //   });
  // }
}