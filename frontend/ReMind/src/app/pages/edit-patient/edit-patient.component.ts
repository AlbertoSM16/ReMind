import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { PacientService } from '../../services/pacient.service';
import { Paciente } from '../../models/paciente.model';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-edit-patient-form',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './edit-patient.component.html',
  styleUrls: ['./edit-patient.component.css']
})
export class EditPatientFormComponent implements OnInit {
  
  patient: Paciente = {} as Paciente;
  patientId: number = 0;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private pacientService: PacientService
  ) { }

  ngOnInit(): void {
    // Captura el ID del paciente de la URL
    this.patientId = +this.route.snapshot.paramMap.get('id')!;
    if (this.patientId) {
      this.loadPatientData();
    }
  }

  loadPatientData(): void {
    this.pacientService.getPatientById(this.patientId).subscribe({
      next: (data) => {
        this.patient = data;
      },
      error: (error) => console.error('Error al cargar datos del paciente', error)
    });
  }

  onSubmit(): void {
    this.pacientService.updatePatient(this.patientId, this.patient).subscribe({
      next: (response) => {
        if (response) {
          Swal.fire({
            title: 'Éxito',
            text: 'Paciente actualizado con éxito!',
            icon: 'success',
            confirmButtonText: 'Aceptar'
          });
          this.router.navigate(['/pacients']);
        }
      },
      error: (error) => {
        if (error.status === 200) {
          alert('Paciente actualizado con éxito!');
          this.router.navigate(['/pacients']);
        } else {
          console.error('Error al actualizar el paciente:', error);
          alert('Hubo un error al actualizar el paciente.');
        }
      }
    });
  }
}