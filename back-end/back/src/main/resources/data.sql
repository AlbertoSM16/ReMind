-- -- PRECARGA DE USUARIOS INICIALES (Contraseña para todos: password123)

-- -- 1. Insertar Administrador
-- -- Nota: rol es TipoUsuario.ADMINISTRADOR = 0 (ordinal)
-- INSERT INTO administrador (nombre, apellido, email, contrasena, telefono, usuario, rol)
-- VALUES ('Admin', 'Sistemas', 'admin@remind.com', '$2a$10$8.Z2rOpR8hIP81Kx4G9xPePy74NpHpB/bB/F3.M1Kk.QnBqP1Wcve', '123456789', 'admin', 0);

-- -- 2. Insertar Terapeuta / Doctor
-- -- Nota: rol es TipoUsuario.TERAPEUTA = 1 (ordinal)
-- INSERT INTO terapeuta (nombre, apellido, email, contrasena, telefono, fecha_nacimiento, usuario, especialidad, rol)
-- VALUES ('Doctor', 'Remind', 'doctor@remind.com', '$2a$10$8.Z2rOpR8hIP81Kx4G9xPePy74NpHpB/bB/F3.M1Kk.QnBqP1Wcve', '987654321', '1980-05-15', 'doctor', 'Neurologia', 1);

-- -- 3. Insertar Paciente
-- -- Nota: rol es TipoUsuario.PACIENTE = 'PACIENTE' (Enum String)
-- INSERT INTO paciente (nombre, apellido, contrasenia, telefono, fecha_nacimiento, usuario, enfermedad, edad, nombre_responsable, rol, terapeuta_id)
-- VALUES ('Paciente', 'Prueba', '$2a$10$8.Z2rOpR8hIP81Kx4G9xPePy74NpHpB/bB/F3.M1Kk.QnBqP1Wcve', '555555555', '1950-10-20', 'paciente', 'Alzheimer', 75, 'Responsable Prueba', 'PACIENTE', 1);
