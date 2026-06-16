import { Component, OnInit, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, NgForm } from '@angular/forms';
import { PacientService } from '../../services/pacient.service';
import { Router, RouterModule } from '@angular/router';
import { Paciente } from '../../models/paciente.model';
import { HeaderComponent } from '../../components/header/header.component';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-create-patient-form',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule, HeaderComponent],
  templateUrl: './create-pacient-form.component.html',
  styleUrls: ['./create-pacient-form.component.css']
})
export class CreatePatientFormComponent implements OnInit {
  @ViewChild('pacientForm') pacientForm!: NgForm;
  pacient: Paciente = {} as Paciente;
  today: string = '';
  submitted = false;

  constructor(
    private pacientService: PacientService,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.today = this.formatDate(new Date());
  }

  private formatDate(date: Date): string {
    const year = date.getFullYear();
    const month = (date.getMonth() + 1).toString().padStart(2, '0');
    const day = date.getDate().toString().padStart(2, '0');
    return `${year}-${month}-${day}`;
  }

  onSubmit(): void {
    this.submitted = true;
    if (this.pacientForm.invalid) {
      Object.values(this.pacientForm.controls).forEach(control => {
        control.markAsTouched();
      });
      return;
    }

    const terapeutaId = sessionStorage.getItem('id');
    if (!terapeutaId) {
      Swal.fire('Error', 'No se pudo identificar al terapeuta. Por favor, inicie sesión de nuevo.', 'error');
      return;
    }
    this.pacient.terapeuta_id = +terapeutaId;

    this.pacientService.createPatient(this.pacient).subscribe({
      next: (response) => {
        Swal.fire({
          title: '¡Paciente Creado!',
          html: `
            <p>El paciente ha sido creado con éxito. Sus datos de acceso son:</p>
            <div style="text-align: left; margin-left: 20px;">
              <strong>Usuario:</strong> ${response.usuario}<br>
              <strong>Contraseña:</strong> ${response.contrasenia}
            </div>
            <br>
            <p>Por favor, guarde estas credenciales en un lugar seguro.</p>
          `,
          icon: 'success',
          confirmButtonText: 'Entendido'
        }).then(() => {
          this.router.navigate(['/pacients']);
        });
      },
      error: (error) => {
        console.error('Error al crear paciente:', error);
        Swal.fire('Error', 'Hubo un problema al crear el paciente.', 'error');
      }
    });
  }
}
