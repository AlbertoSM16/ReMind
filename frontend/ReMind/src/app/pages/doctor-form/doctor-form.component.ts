import { Component, OnInit } from '@angular/core';
import { AbstractControl, FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { TerapeutaService } from '../../services/terapeuta.service';
import { HeaderComponent } from '../../components/header/header.component';
import { CommonModule } from '@angular/common';
import Swal from 'sweetalert2';

function noFutureDate(control: AbstractControl): { [key: string]: boolean } | null {
  if (!control.value) return null;
  const inputDate = new Date(control.value);
  const today = new Date();
  today.setHours(23, 59, 59, 999);
  return inputDate > today ? { futureDate: true } : null;
}

@Component({
  selector: 'app-doctor-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterModule, HeaderComponent],
  templateUrl: './doctor-form.component.html',
  styleUrls: ['./doctor-form.component.css']
})
export class DoctorFormComponent implements OnInit {
  doctorForm: FormGroup;
  isEditMode = false;
  terapeutaId: number | null = null;
  errorMessage: string | null = null;
  submitted = false;

  constructor(
    private fb: FormBuilder,
    private terapeutaService: TerapeutaService,
    private router: Router,
    private route: ActivatedRoute
  ) {
    this.doctorForm = this.fb.group({
      nombre: ['', [Validators.required, Validators.pattern('.*\\S.*'), Validators.maxLength(100)]],
      apellido: ['', [Validators.required, Validators.pattern('.*\\S.*'), Validators.maxLength(100)]],
      correo: ['', [Validators.required, Validators.email]],
      telefono: ['', [Validators.pattern('^\\d{9}$')]],
      especialidad: ['', [Validators.required, Validators.pattern('.*\\S.*'), Validators.maxLength(100)]],
      fechaNacimiento: ['', [Validators.required, noFutureDate]]
    });
  }

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.isEditMode = true;
      this.terapeutaId = +id;
      this.terapeutaService.getTerapeutaById(this.terapeutaId).subscribe(data => {
        const formattedData = {
          ...data,
          fechaNacimiento: this.formatDate(data.fechaNacimiento)
        };
        this.doctorForm.patchValue(formattedData);
      });
    }
  }

  private formatDate(dateString: string): string {
    if (!dateString) return '';
    const date = new Date(dateString);
    const year = date.getFullYear();
    const month = (date.getMonth() + 1).toString().padStart(2, '0');
    const day = date.getDate().toString().padStart(2, '0');
    return `${year}-${month}-${day}`;
  }

  onSubmit(): void {
    this.submitted = true;
    if (this.doctorForm.invalid) {
      Object.values(this.doctorForm.controls).forEach(control => {
        control.markAsTouched();
      });
      return;
    }

    const formData = {
      ...this.doctorForm.value,
      fechaNacimiento: this.formatDate(this.doctorForm.value.fechaNacimiento)
    };

    if (this.isEditMode && this.terapeutaId) {
      this.terapeutaService.updateTerapeuta(this.terapeutaId, formData).subscribe({
        next: () => {
          Swal.fire({
            title: 'Doctor actualizado!',
            text: 'El doctor ha sido actualizado con éxito.',
            icon: 'success',
            confirmButtonText: 'Entendido'
          }).then(() => {
            this.router.navigate(['/doctors']);
          });
        },
        error: () => this.errorMessage = 'Error al actualizar el terapeuta.'
      });
    } else {
      this.terapeutaService.createTerapeuta(formData).subscribe({
        next: (response) => {
          Swal.fire({
            title: 'Doctor Creado!',
            html: `
              <p>El doctor ha sido creado con éxito. Sus datos de acceso son:</p>
              <div style="text-align: left; margin-left: 20px;">
                <strong>Usuario:</strong> ${response.usuario}<br>
                <strong>Contraseña:</strong> ${response.contrasena}
              </div>
              <br>
              <p>Por favor, guarde estas credenciales en un lugar seguro.</p>
            `,
            icon: 'success',
            confirmButtonText: 'Entendido'
          }).then(() => {
            this.router.navigate(['/doctors']);
          });
        },
        error: () => this.errorMessage = 'Error al crear el terapeuta.'
      });
    }
  }
}
