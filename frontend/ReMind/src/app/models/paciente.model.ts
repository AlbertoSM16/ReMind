export interface Paciente {
  id:number;
  nombre: string;
  apellido: string;
  contrasenia: string;
  telefono: string;
  enfermedad: string; 
  edad: number;
  nombreResponsable: string;
  fechaNacimiento: string; 
  terapeuta_id: number;

  usuario?: string; 
}