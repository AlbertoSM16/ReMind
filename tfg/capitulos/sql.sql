CREATE DATABASE remind;
USE remind;

CREATE TABLE `administrador` (
  `id` int NOT NULL AUTO_INCREMENT,
  `apellido` varchar(255) DEFAULT NULL,
  `contrasena` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `nombre` varchar(255) DEFAULT NULL,
  `rol` tinyint DEFAULT NULL,
  `telefono` varchar(255) DEFAULT NULL,
  `usuario` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_r0jus8ywt1dl6snd357ie37j9` (`usuario`),
  CONSTRAINT `administrador_chk_1` CHECK ((`rol` between 0 and 2))
);

CREATE TABLE `agenda` (
  `id` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
);

CREATE TABLE `terapeuta` (
  `id` int NOT NULL AUTO_INCREMENT,
  `apellido` varchar(255) DEFAULT NULL,
  `contrasena` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `especialidad` varchar(255) DEFAULT NULL,
  `fecha_nacimiento` datetime(6) NOT NULL,
  `nombre` varchar(255) DEFAULT NULL,
  `rol` tinyint DEFAULT NULL,
  `telefono` varchar(255) DEFAULT NULL,
  `usuario` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_es1my8pktqtf6hx8pfmwrih3i` (`usuario`),
  CONSTRAINT `terapeuta_chk_1` CHECK ((`rol` between 0 and 2))
);

CREATE TABLE `agenda_terapeuta` (
  `id` int NOT NULL AUTO_INCREMENT,
  `agenda_id` int DEFAULT NULL,
  `terapeuta_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_vew38miuawehnbuk872hshyq` (`agenda_id`),
  KEY `FKpju98yotcaxmtcnq1t6my31ds` (`terapeuta_id`),
  CONSTRAINT `FK76snub78o5k3o5jwtj4g0aceb` FOREIGN KEY (`agenda_id`) REFERENCES `agenda` (`id`),
  CONSTRAINT `FKpju98yotcaxmtcnq1t6my31ds` FOREIGN KEY (`terapeuta_id`) REFERENCES `terapeuta` (`id`)
);

CREATE TABLE `juego` (
  `id` int NOT NULL AUTO_INCREMENT,
  `codigo` varchar(255) DEFAULT NULL,
  `nombre` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
);

CREATE TABLE `juego_agenda` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `dificultad` int NOT NULL,
  `fecha_asignacion` date NOT NULL,
  `fecha_realizacion` date DEFAULT NULL,
  `realizado` bit(1) NOT NULL,
  `agenda_id` int DEFAULT NULL,
  `juego_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKgsuan9i0llw4lwjc61gq859af` (`agenda_id`),
  KEY `FK1y5eoloxccwg13015emsgdxus` (`juego_id`),
  CONSTRAINT `FK1y5eoloxccwg13015emsgdxus` FOREIGN KEY (`juego_id`) REFERENCES `juego` (`id`),
  CONSTRAINT `FKgsuan9i0llw4lwjc61gq859af` FOREIGN KEY (`agenda_id`) REFERENCES `agenda` (`id`)
);

CREATE TABLE `paciente` (
  `id` int NOT NULL AUTO_INCREMENT,
  `apellido` varchar(255) DEFAULT NULL,
  `contrasenia` varchar(255) DEFAULT NULL,
  `edad` int NOT NULL,
  `enfermedad` varchar(255) DEFAULT NULL,
  `fecha_nacimiento` datetime(6) NOT NULL,
  `nombre` varchar(255) DEFAULT NULL,
  `nombre_responsable` varchar(255) DEFAULT NULL,
  `rol` enum('ADMINISTRADOR','TERAPEUTA','PACIENTE') DEFAULT NULL,
  `telefono` varchar(255) DEFAULT NULL,
  `usuario` varchar(255) DEFAULT NULL,
  `terapeuta_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_j1spcjtos8756td9bybqhwutv` (`usuario`),
  KEY `FKtknho0gp5rf74qx2e1olqxt0d` (`terapeuta_id`),
  CONSTRAINT `FKtknho0gp5rf74qx2e1olqxt0d` FOREIGN KEY (`terapeuta_id`) REFERENCES `terapeuta` (`id`)
);

CREATE TABLE `paciente_agenda` (
  `id` int NOT NULL AUTO_INCREMENT,
  `fecha` datetime(6) DEFAULT NULL,
  `agenda_id` int DEFAULT NULL,
  `paciente_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKgs0v42fbsta3gogyxxtb1si4k` (`agenda_id`),
  KEY `FKdrhkw9624nobtxn78od9t6p1b` (`paciente_id`),
  CONSTRAINT `FKdrhkw9624nobtxn78od9t6p1b` FOREIGN KEY (`paciente_id`) REFERENCES `paciente` (`id`),
  CONSTRAINT `FKgs0v42fbsta3gogyxxtb1si4k` FOREIGN KEY (`agenda_id`) REFERENCES `agenda` (`id`)
);

CREATE TABLE `paciente_juego` (
  `id` int NOT NULL AUTO_INCREMENT,
  `completado` bit(1) DEFAULT NULL,
  `fecha_realizacion` date DEFAULT NULL,
  `juego_id` int DEFAULT NULL,
  `paciente_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKw50s94j2i8a1n6pkyttqd72y` (`juego_id`),
  KEY `FKid6vaedfohldlshe24t07fnid` (`paciente_id`),
  CONSTRAINT `FKid6vaedfohldlshe24t07fnid` FOREIGN KEY (`paciente_id`) REFERENCES `paciente` (`id`),
  CONSTRAINT `FKw50s94j2i8a1n6pkyttqd72y` FOREIGN KEY (`juego_id`) REFERENCES `juego` (`id`)
);

CREATE TABLE `paciente_terapeuta` (
  `id` int NOT NULL AUTO_INCREMENT,
  `paciente_id` int DEFAULT NULL,
  `terapeuta_id` int NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_sq8p1wp2c8i8v7fsk4xuggpoo` (`paciente_id`),
  KEY `FK3uqnp2gfv3skbu64jgt3sx78i` (`terapeuta_id`),
  CONSTRAINT `FK3uqnp2gfv3skbu64jgt3sx78i` FOREIGN KEY (`terapeuta_id`) REFERENCES `terapeuta` (`id`),
  CONSTRAINT `FKqdh3mlns7tsm2rv70uchmgq6m` FOREIGN KEY (`paciente_id`) REFERENCES `paciente` (`id`)
);