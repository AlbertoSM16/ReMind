import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { TerapeutaService } from '../../services/terapeuta.service';
import { CommonModule } from '@angular/common';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-doctor-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterModule],
  templateUrl: './doctor-form.component.html',
  styleUrls: ['./doctor-form.component.css']
})
export class DoctorFormComponent implements OnInit {
  doctorForm: FormGroup;
  isEditMode = false;
  terapeutaId: number | null = null;
  errorMessage: string | null = null;
  successMessage: string | null = null;

  constructor(
    private fb: FormBuilder,
    private terapeutaService: TerapeutaService,
    private router: Router,
    private route: ActivatedRoute
  ) {
    this.doctorForm = this.fb.group({
      nombre: ['', Validators.required],
      apellido: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      telefono: [''],
      especialidad: ['', Validators.required],
      fechaNacimiento: ['', Validators.required]

    });
  }

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.isEditMode = true;
      this.terapeutaId = +id;
      this.terapeutaService.getTerapeutaById(this.terapeutaId).subscribe(data => {
        // Format the date for the input field
        const formattedData = {
          ...data,
          fechaNacimiento: this.formatDateForInput(data.fechaNacimiento)
        };
        this.doctorForm.patchValue(formattedData);
      });
    }
  }
  private formatDateForInput(dateString: string): string {
    if (!dateString) return '';

    const date = new Date(dateString);
    const year = date.getFullYear();
    const month = (date.getMonth() + 1).toString().padStart(2, '0');
    const day = date.getDate().toString().padStart(2, '0');
    return `${year}-${month}-${day}`;
  }

  private formatDateForBackend(dateString: string): string {
    if (!dateString) return '';

    const date = new Date(dateString);
    const year = date.getFullYear();
    const month = (date.getMonth() + 1).toString().padStart(2, '0');
    const day = date.getDate().toString().padStart(2, '0');
    return `${year}-${month}-${day}`;
  }

  onSubmit(): void {
    if (this.doctorForm.invalid) {
      return;
    }

    const formData = {
      ...this.doctorForm.value,
      fechaNacimiento: this.formatDateForBackend(this.doctorForm.value.fechaNacimiento)
    };

    if (this.isEditMode && this.terapeutaId) {
      this.terapeutaService.updateTerapeuta(this.terapeutaId, formData).subscribe({
        next: (response) => {
          Swal.fire({
            title: 'Doctor actualizado!',
            html: `
                      <p>El doctor ha sido actualizado con éxito.</p>
                      
                    `,
            icon: 'success',
            confirmButtonText: 'Entendido'
          }).then(() => {
            this.router.navigate(['/doctors']);
          })
        },
        error: (err) => this.errorMessage = 'Error al actualizar el terapeuta.'
      });
    } else {
      console.log(formData);
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
          })
        },
        error: (err) => this.errorMessage = 'Error al crear el terapeuta.'
      });
    }
  }
}