-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Versión del servidor: 10.4.32-MariaDB
-- Versión de PHP: 8.2.12
--

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

CREATE DATABASE IF NOT EXISTS `bdfe_bruno` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE `bdfe_bruno`;

CREATE TABLE IF NOT EXISTS `user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `dtype` varchar(31) DEFAULT NULL,
  `dob` date DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `first_name` varchar(255) DEFAULT NULL,
  `gender` varchar(255) DEFAULT NULL,
  `last_name` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `role` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKob8kqyqqgmefl0aco34akdtpe` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE IF NOT EXISTS `empresa` (
  `id_empresa` bigint(20) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(255) DEFAULT NULL,
  `direccion` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id_empresa`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE IF NOT EXISTS `ciclo_formativo` (
  `id_ciclo` bigint(20) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(255) DEFAULT NULL,
  `descripcion` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`id_ciclo`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE IF NOT EXISTS `curso` (
  `id_curso` bigint(20) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(255) DEFAULT NULL,
  `anio` int(11) DEFAULT NULL,
  `ciclo_formativo_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id_curso`),
  KEY `fk_curso_ciclo` (`ciclo_formativo_id`),
  CONSTRAINT `fk_curso_ciclo` FOREIGN KEY (`ciclo_formativo_id`) REFERENCES `ciclo_formativo` (`id_ciclo`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE IF NOT EXISTS `administrador` (
  `id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_administrador_user` FOREIGN KEY (`id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE IF NOT EXISTS `profesor` (
  `id` bigint(20) NOT NULL,
  `es_coordinador` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_profesor_user` FOREIGN KEY (`id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE IF NOT EXISTS `tutor` (
  `id` bigint(20) NOT NULL,
  `telefono` varchar(20) DEFAULT NULL,
  `empresa_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_tutor_empresa` (`empresa_id`),
  CONSTRAINT `fk_tutor_user` FOREIGN KEY (`id`) REFERENCES `user` (`id`),
  CONSTRAINT `fk_tutor_empresa` FOREIGN KEY (`empresa_id`) REFERENCES `empresa` (`id_empresa`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE IF NOT EXISTS `estudiante` (
  `id` bigint(20) NOT NULL,
  `curso_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_estudiante_curso` (`curso_id`),
  CONSTRAINT `fk_estudiante_user` FOREIGN KEY (`id`) REFERENCES `user` (`id`),
  CONSTRAINT `fk_estudiante_curso` FOREIGN KEY (`curso_id`) REFERENCES `curso` (`id_curso`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE IF NOT EXISTS `modulo` (
  `id_modulo` bigint(20) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(255) DEFAULT NULL,
  `codigo` varchar(50) DEFAULT NULL,
  `horas` int(11) DEFAULT NULL,
  `curso_id` bigint(20) DEFAULT NULL,
  `profesor_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id_modulo`),
  KEY `fk_modulo_curso` (`curso_id`),
  KEY `fk_modulo_profesor` (`profesor_id`),
  CONSTRAINT `fk_modulo_curso` FOREIGN KEY (`curso_id`) REFERENCES `curso` (`id_curso`),
  CONSTRAINT `fk_modulo_profesor` FOREIGN KEY (`profesor_id`) REFERENCES `profesor` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE IF NOT EXISTS `formacion_empresa` (
  `id_formacion` bigint(20) NOT NULL AUTO_INCREMENT,
  `fecha_inicio` date DEFAULT NULL,
  `fecha_fin` date DEFAULT NULL,
  `estado` varchar(50) DEFAULT NULL,
  `estudiante_id` bigint(20) DEFAULT NULL,
  `profesor_id` bigint(20) DEFAULT NULL,
  `tutor_id` bigint(20) DEFAULT NULL,
  `curso_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id_formacion`),
  KEY `fk_formacion_estudiante` (`estudiante_id`),
  KEY `fk_formacion_profesor` (`profesor_id`),
  KEY `fk_formacion_tutor` (`tutor_id`),
  KEY `fk_formacion_curso` (`curso_id`),
  CONSTRAINT `fk_formacion_estudiante` FOREIGN KEY (`estudiante_id`) REFERENCES `estudiante` (`id`),
  CONSTRAINT `fk_formacion_profesor` FOREIGN KEY (`profesor_id`) REFERENCES `profesor` (`id`),
  CONSTRAINT `fk_formacion_tutor` FOREIGN KEY (`tutor_id`) REFERENCES `tutor` (`id`),
  CONSTRAINT `fk_formacion_curso` FOREIGN KEY (`curso_id`) REFERENCES `curso` (`id_curso`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE IF NOT EXISTS `documento` (
  `id_documento` bigint(20) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(255) DEFAULT NULL,
  `tipo` varchar(100) DEFAULT NULL,
  `ruta` varchar(500) DEFAULT NULL,
  `formacion_empresa_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id_documento`),
  KEY `fk_documento_formacion` (`formacion_empresa_id`),
  CONSTRAINT `fk_documento_formacion` FOREIGN KEY (`formacion_empresa_id`) REFERENCES `formacion_empresa` (`id_formacion`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

INSERT INTO `user` (`id`, `dtype`, `dob`, `email`, `first_name`, `gender`, `last_name`, `password`, `role`) VALUES
(1, 'Administrador', '1980-01-15', 'admin', 'Administrador', 'Masculino', 'Sistema', 'admin', 'Administrador'),
(2, 'Profesor', '1975-05-20', 'profesor', 'Juan', 'Masculino', 'García López', 'profesor', 'Profesor/Tutor'),
(3, 'Tutor', '1982-08-10', 'tutor_empresa', 'María', 'Femenino', 'Fernández Ruiz', 'tutor', 'Tutor de Empresa'),
(4, 'Estudiante', '2000-03-25', 'estudiante', 'Carlos', 'Masculino', 'Martínez Sánchez', 'estudiante', 'Estudiante'),
(5, 'Profesor', '1978-11-08', 'profesor2', 'Ana', 'Femenino', 'Pérez González', 'profesor2', 'Profesor/Tutor'),
(6, 'Tutor', '1985-04-12', 'tutor_empresa2', 'Pedro', 'Masculino', 'López Díaz', 'tutor2', 'Tutor de Empresa'),
(7, 'Estudiante', '2001-07-30', 'estudiante2', 'Laura', 'Femenino', 'Rodríguez Hernández', 'estudiante2', 'Estudiante');

INSERT INTO `administrador` (`id`) VALUES (1);
INSERT INTO `profesor` (`id`, `es_coordinador`) VALUES (2, 1), (5, 0);
INSERT INTO `tutor` (`id`, `telefono`) VALUES (3, NULL), (6, NULL);
INSERT INTO `estudiante` (`id`, `curso_id`) VALUES (4, NULL), (7, NULL);

INSERT INTO `empresa` (`id_empresa`, `nombre`, `direccion`) VALUES
(1, 'Empresa 1', 'Calle Alarcon 10, Gijón'),
(2, 'Empresa 2', 'Avda de la Constitución 3, Gijón');

INSERT INTO `ciclo_formativo` (`id_ciclo`, `nombre`, `descripcion`) VALUES
(1, 'Desarrollo de Aplicaciones Multiplataforma', 'Ciclo formativo de grado superior DAM'),
(2, 'Desarrollo de Aplicaciones Web', 'Ciclo formativo de grado superior DAW'),
(3, 'Administración de Sistemas Informáticos en Red', 'Ciclo formativo de grado superior ASIR');

INSERT INTO `curso` (`id_curso`, `nombre`, `anio`, `ciclo_formativo_id`) VALUES
(1, '2º DAM 2025/2026', 2025, 1),
(2, '2º DAW 2025/2026', 2025, 2),
(3, '2º ASIR 2025/2026', 2025, 3);

INSERT INTO `modulo` (`id_modulo`, `nombre`, `codigo`, `horas`, `curso_id`, `profesor_id`) VALUES
(1, 'Acceso a Datos', 'AD', 120, 2, 2),
(2, 'Desarrollo de Interfaces', 'DI', 140, 2, 5);
(3, 'Despliegue de Aplicaciones Web', 'DAW', 100, 1, 2),
(4, 'Desarrollo Web en Entorno Cliente', 'DWEC', 140, 1, 5),
(5, 'Desarrollo Web en Entorno Servidor', 'DWES', 160, 1, 2),

COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
