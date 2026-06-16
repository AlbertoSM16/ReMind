import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { PacientService } from '../../services/pacient.service';
import { Paciente } from '../../models/paciente.model';
import Swal from 'sweetalert2';
import { HeaderComponent } from "../../components/header/header.component";

@Component({
  selector: 'app-edit-patient-form',
  standalone: true,
  imports: [CommonModule, FormsModule, HeaderComponent],
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
      error: (error) =>  Swal.fire({
            title: 'Error',
            text: 'Error al obtener los datos del paciente!',
            icon: 'error',
            confirmButtonText: 'Aceptar'
          })
    });
  }

  onSubmit(): void {
    this.pacientService.updatePatient(this.patientId, this.patient).subscribe({
      next: () => {
        Swal.fire({
          title: 'Éxito',
          text: 'Paciente actualizado con éxito!',
          icon: 'success',
          confirmButtonText: 'Aceptar'
        });
        this.router.navigate(['/pacients']);
      },
      error: () => {
        Swal.fire({
          title: 'Error',
          text: 'Hubo un error al actualizar el paciente.',
          icon: 'error',
          confirmButtonText: 'Aceptar'
        });
      }
    });
  }
}