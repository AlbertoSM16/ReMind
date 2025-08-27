import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Terapeuta } from '../../models/terapeuta.model';
import { TerapeutaService } from '../../services/terapeuta.service';

@Component({
  selector: 'app-edit-terapeuta-form',
  templateUrl: './edit-terapeuta.component.html',
  styleUrls: ['./edit-terapeuta.component.css']
})
export class EditTerapeutaComponent  {

  // terapeuta: Terapeuta = {
  //   id: 0,
  //   nombre: '',
  //   correo: '',
  //   especialidad: ''
  // };

  // constructor(
  //   private terapeutaService: TerapeutaService,
  //   private route: ActivatedRoute,
  //   private router: Router
  // ) { }

  // ngOnInit(): void {
  //   const id = this.route.snapshot.paramMap.get('id');
  //   if (id) {
  //     this.terapeutaService.getTerapeutaById(+id).subscribe(terapeuta => {
  //       this.terapeuta = terapeuta;
  //     });
  //   }
  // }

  // updateTerapeuta(): void {
  //   this.terapeutaService.updateTerapeuta(this.terapeuta.id!, this.terapeuta).subscribe(() => {
  //     this.router.navigate(['/terapeutas']); // O a donde quieras redirigir
  //   });
  // }
}