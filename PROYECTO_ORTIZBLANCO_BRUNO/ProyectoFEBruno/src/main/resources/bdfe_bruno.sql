-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 13-02-2026 a las 17:20:07
-- Versión del servidor: 10.4.32-MariaDB
-- Versión de PHP: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `bdfe_bruno`
--
CREATE DATABASE IF NOT EXISTS `bdfe_bruno` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE `bdfe_bruno`;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `administrador`
--

CREATE TABLE IF NOT EXISTS `administrador` (
  `id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `administrador`
--

INSERT INTO `administrador` (`id`) VALUES
(1);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `ciclo_formativo`
--

CREATE TABLE IF NOT EXISTS `ciclo_formativo` (
  `id_ciclo` bigint(20) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(255) DEFAULT NULL,
  `descripcion` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id_ciclo`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `ciclo_formativo`
--

INSERT INTO `ciclo_formativo` (`id_ciclo`, `nombre`, `descripcion`) VALUES
(1, 'Desarrollo de Aplicaciones Web', 'Ciclo formativo de grado superior DAW'),
(2, 'Desarrollo de Aplicaciones Multiplataforma', 'Ciclo formativo de grado superior DAM'),
(3, 'Administración de Sistemas Informáticos en Red', 'Ciclo formativo de grado superior ASIR'),
(4, 'VIFC302', 'Ciclo formativo VIFC302'),
(5, 'VIFC303', 'Ciclo formativo VIFC303');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `curso`
--

CREATE TABLE IF NOT EXISTS `curso` (
  `id_curso` bigint(20) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(255) DEFAULT NULL,
  `anio` int(11) DEFAULT NULL,
  `ciclo_formativo_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id_curso`),
  KEY `fk_curso_ciclo` (`ciclo_formativo_id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `curso`
--

INSERT INTO `curso` (`id_curso`, `nombre`, `anio`, `ciclo_formativo_id`) VALUES
(1, '2º DAW 2025/2026', 2025, 1),
(2, '2º DAM 2025/2026', 2025, 2),
(3, '2º ASIR 2025/2026', 2025, 3),
(4, '1VIFC302', 2025, 4),
(5, '2VIFC302', 2025, 4),
(6, '1VIFC303', 2025, 5),
(7, '2VIFC303', 2025, 5);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `documento`
--

CREATE TABLE IF NOT EXISTS `documento` (
  `id_documento` bigint(20) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(255) DEFAULT NULL,
  `tipo` varchar(255) DEFAULT NULL,
  `ruta` varchar(255) DEFAULT NULL,
  `formacion_empresa_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id_documento`),
  KEY `fk_documento_formacion` (`formacion_empresa_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `empresa`
--

CREATE TABLE IF NOT EXISTS `empresa` (
  `id_empresa` bigint(20) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(255) DEFAULT NULL,
  `direccion` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id_empresa`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `empresa`
--

INSERT INTO `empresa` (`id_empresa`, `nombre`, `direccion`) VALUES
(1, 'Empresa Tecnológica S.L.', 'Calle Principal 10, Gijón'),
(2, 'Desarrollo Digital S.A.', 'Avenida de la Constitución 25, Oviedo'),
(3, 'AsturCode Solutions S.L.', 'Calle Uría 18, Oviedo'),
(4, 'NorteData Innovación S.A.', 'Parque Tecnológico 7, Gijón'),
(5, 'SoftAstur Consultoría S.L.', 'Calle Marqués de San Esteban 12, Gijón'),
(6, 'Cantábrico Apps S.L.', 'Avenida Galicia 44, Avilés'),
(7, 'Nalón Sistemas S.A.', 'Polígono de Riaño 3, Langreo'),
(8, 'Oviedo Cloud Services S.L.', 'Calle Fruela 9, Oviedo'),
(9, 'Costa Verde Software S.L.', 'Avenida de la Costa 28, Gijón');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `estudiante`
--

CREATE TABLE IF NOT EXISTS `estudiante` (
  `id` bigint(20) NOT NULL,
  `curso_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_estudiante_curso` (`curso_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `estudiante`
--

INSERT INTO `estudiante` (`id`, `curso_id`) VALUES
(4, NULL),
(7, NULL),
(10, 1),
(11, 1),
(12, 1),
(13, 1),
(14, 1),
(15, 2),
(16, 2),
(17, 2),
(18, 2),
(19, 2),
(20, 3),
(21, 3),
(22, 3),
(23, 3),
(24, 3),
(25, 4),
(26, 4),
(27, 4),
(28, 4),
(29, 4),
(30, 5),
(31, 5),
(32, 5),
(33, 5),
(34, 5),
(35, 6),
(36, 6),
(37, 6),
(38, 7),
(39, 7),
(55, 1),
(56, 1),
(57, 2),
(58, 2),
(59, 3),
(60, 3),
(61, 4),
(62, 5),
(63, 6),
(64, 7);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `formacion_empresa`
--

CREATE TABLE IF NOT EXISTS `formacion_empresa` (
  `id_formacion` bigint(20) NOT NULL AUTO_INCREMENT,
  `fecha_inicio` date DEFAULT NULL,
  `fecha_fin` date DEFAULT NULL,
  `estado` varchar(255) DEFAULT NULL,
  `estudiante_id` bigint(20) DEFAULT NULL,
  `profesor_id` bigint(20) DEFAULT NULL,
  `tutor_id` bigint(20) DEFAULT NULL,
  `curso_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id_formacion`),
  KEY `fk_formacion_estudiante` (`estudiante_id`),
  KEY `fk_formacion_profesor` (`profesor_id`),
  KEY `fk_formacion_tutor` (`tutor_id`),
  KEY `fk_formacion_curso` (`curso_id`)
) ENGINE=InnoDB AUTO_INCREMENT=110 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `formacion_empresa`
--

INSERT INTO `formacion_empresa` (`id_formacion`, `fecha_inicio`, `fecha_fin`, `estado`, `estudiante_id`, `profesor_id`, `tutor_id`, `curso_id`) VALUES
(100, '2026-03-02', '2026-06-12', 'Activa', 55, 40, 50, 1),
(101, '2026-03-02', '2026-06-12', 'Activa', 56, 41, 51, 1),
(102, '2026-03-09', '2026-06-19', 'Activa', 57, 42, 52, 2),
(103, '2026-03-09', '2026-06-19', 'Activa', 58, 43, 53, 2),
(104, '2026-03-16', '2026-06-26', 'Activa', 59, 44, 54, 3),
(105, '2026-03-16', '2026-06-26', 'Activa', 60, 45, 50, 3),
(106, '2026-03-23', '2026-07-03', 'Activa', 61, 46, 51, 4),
(107, '2026-03-23', '2026-07-03', 'Activa', 62, 47, 52, 5),
(108, '2026-03-30', '2026-07-10', 'Activa', 63, 48, 53, 6),
(109, '2026-03-30', '2026-07-10', 'Activa', 64, 49, 54, 7);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `modulo`
--

CREATE TABLE IF NOT EXISTS `modulo` (
  `id_modulo` bigint(20) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(255) DEFAULT NULL,
  `codigo` varchar(255) DEFAULT NULL,
  `horas` int(11) DEFAULT NULL,
  `curso_id` bigint(20) DEFAULT NULL,
  `profesor_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id_modulo`),
  KEY `fk_modulo_curso` (`curso_id`),
  KEY `fk_modulo_profesor` (`profesor_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `modulo`
--

INSERT INTO `modulo` (`id_modulo`, `nombre`, `codigo`, `horas`, `curso_id`, `profesor_id`) VALUES
(1, 'Desarrollo Web en Entorno Servidor', 'DWES', 160, 1, 2),
(2, 'Desarrollo Web en Entorno Cliente', 'DWEC', 140, 1, 5),
(3, 'Despliegue de Aplicaciones Web', 'DAW', 100, 1, 2),
(4, 'Acceso a Datos', 'AD', 120, 2, 2),
(5, 'Desarrollo de Interfaces', 'DI', 140, 2, 5);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `profesor`
--

CREATE TABLE IF NOT EXISTS `profesor` (
  `id` bigint(20) NOT NULL,
  `es_coordinador` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `profesor`
--

INSERT INTO `profesor` (`id`, `es_coordinador`) VALUES
(2, 1),
(5, 0),
(40, 1),
(41, 0),
(42, 0),
(43, 0),
(44, 0),
(45, 1),
(46, 0),
(47, 0),
(48, 0),
(49, 0);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `tutor`
--

CREATE TABLE IF NOT EXISTS `tutor` (
  `id` bigint(20) NOT NULL,
  `telefono` varchar(255) DEFAULT NULL,
  `empresa_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_tutor_empresa` (`empresa_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `tutor`
--

INSERT INTO `tutor` (`id`, `telefono`, `empresa_id`) VALUES
(3, NULL, NULL),
(6, NULL, NULL),
(8, '684111222', 3),
(9, '684333444', 4),
(50, '684555101', 5),
(51, '684555102', 6),
(52, '684555103', 7),
(53, '684555104', 8),
(54, '684555105', 9);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `user`
--

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
) ENGINE=InnoDB AUTO_INCREMENT=65 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `user`
--

INSERT INTO `user` (`id`, `dtype`, `dob`, `email`, `first_name`, `gender`, `last_name`, `password`, `role`) VALUES
(1, 'Administrador', '1980-01-15', 'admin', 'Administrador', 'Masculino', 'Sistema', 'admin', 'Administrador'),
(2, 'Profesor', '1975-05-20', 'profesor', 'Juan', 'Masculino', 'García López', 'profesor', 'Profesor/Tutor'),
(3, 'Tutor', '1982-08-10', 'tutor_empresa', 'María', 'Femenino', 'Fernández Ruiz', 'tutor', 'Tutor de Empresa'),
(4, 'Estudiante', '2000-03-25', 'estudiante', 'Carlos', 'Masculino', 'Martínez Sánchez', 'estudiante', 'Estudiante'),
(5, 'Profesor', '1978-11-08', 'profesor2', 'Ana', 'Femenino', 'Pérez González', 'profesor2', 'Profesor/Tutor'),
(6, 'Tutor', '1985-04-12', 'tutor_empresa2', 'Pedro', 'Masculino', 'López Díaz', 'tutor2', 'Tutor de Empresa'),
(7, 'Estudiante', '2001-07-30', 'estudiante2', 'Laura', 'Femenino', 'Rodríguez Hernández', 'estudiante2', 'Estudiante'),
(8, 'Tutor', '1988-01-01', 'laura.tutor', 'Laura', 'Femenino', 'Menéndez Álvarez', 'laura123', 'Tutor de Empresa'),
(9, 'Tutor', '1988-01-01', 'diego.tutor', 'Diego', 'Masculino', 'Suárez Blanco', 'diego123', 'Tutor de Empresa'),
(10, 'Estudiante', '2003-02-14', 'alumno01', 'Adrián', 'Masculino', 'Alonso Vega', 'alumno01', 'Estudiante'),
(11, 'Estudiante', '2003-05-21', 'alumno02', 'Lucía', 'Femenino', 'Álvarez Cano', 'alumno02', 'Estudiante'),
(12, 'Estudiante', '2002-09-03', 'alumno03', 'Pablo', 'Masculino', 'Arias Muñiz', 'alumno03', 'Estudiante'),
(13, 'Estudiante', '2004-01-17', 'alumno04', 'Sara', 'Femenino', 'Blanco Torres', 'alumno04', 'Estudiante'),
(14, 'Estudiante', '2003-11-28', 'alumno05', 'Diego', 'Masculino', 'Cabrera Soto', 'alumno05', 'Estudiante'),
(15, 'Estudiante', '2002-04-09', 'alumno06', 'Noa', 'Femenino', 'Castro Iglesias', 'alumno06', 'Estudiante'),
(16, 'Estudiante', '2003-07-19', 'alumno07', 'Hugo', 'Masculino', 'Díaz Menéndez', 'alumno07', 'Estudiante'),
(17, 'Estudiante', '2004-03-12', 'alumno08', 'Carmen', 'Femenino', 'Fernández Prieto', 'alumno08', 'Estudiante'),
(18, 'Estudiante', '2003-06-30', 'alumno09', 'Marcos', 'Masculino', 'García Suárez', 'alumno09', 'Estudiante'),
(19, 'Estudiante', '2002-12-05', 'alumno10', 'Paula', 'Femenino', 'González Cueto', 'alumno10', 'Estudiante'),
(20, 'Estudiante', '2004-08-22', 'alumno11', 'Iván', 'Masculino', 'Gutiérrez Rivas', 'alumno11', 'Estudiante'),
(21, 'Estudiante', '2003-10-11', 'alumno12', 'Marta', 'Femenino', 'López Noriega', 'alumno12', 'Estudiante'),
(22, 'Estudiante', '2002-01-29', 'alumno13', 'Álvaro', 'Masculino', 'Martín Vidal', 'alumno13', 'Estudiante'),
(23, 'Estudiante', '2003-04-18', 'alumno14', 'Elena', 'Femenino', 'Morán Castaño', 'alumno14', 'Estudiante'),
(24, 'Estudiante', '2004-07-07', 'alumno15', 'Sergio', 'Masculino', 'Navarro Solís', 'alumno15', 'Estudiante'),
(25, 'Estudiante', '2002-10-24', 'alumno16', 'Irene', 'Femenino', 'Ortega Palacio', 'alumno16', 'Estudiante'),
(26, 'Estudiante', '2003-12-16', 'alumno17', 'Mario', 'Masculino', 'Paredes Valdés', 'alumno17', 'Estudiante'),
(27, 'Estudiante', '2004-05-02', 'alumno18', 'Claudia', 'Femenino', 'Peña Robles', 'alumno18', 'Estudiante'),
(28, 'Estudiante', '2002-08-13', 'alumno19', 'Daniel', 'Masculino', 'Pérez Llaneza', 'alumno19', 'Estudiante'),
(29, 'Estudiante', '2003-03-27', 'alumno20', 'Aitana', 'Femenino', 'Ramos Rivero', 'alumno20', 'Estudiante'),
(30, 'Estudiante', '2004-09-15', 'alumno21', 'Javier', 'Masculino', 'Rodríguez Barreiro', 'alumno21', 'Estudiante'),
(31, 'Estudiante', '2002-11-06', 'alumno22', 'Natalia', 'Femenino', 'Romero Montes', 'alumno22', 'Estudiante'),
(32, 'Estudiante', '2003-01-23', 'alumno23', 'Rubén', 'Masculino', 'Sánchez Llano', 'alumno23', 'Estudiante'),
(33, 'Estudiante', '2004-06-04', 'alumno24', 'Vega', 'Femenino', 'Serrano Varela', 'alumno24', 'Estudiante'),
(34, 'Estudiante', '2002-02-26', 'alumno25', 'Samuel', 'Masculino', 'Suárez Mieres', 'alumno25', 'Estudiante'),
(35, 'Estudiante', '2003-08-08', 'alumno26', 'Nerea', 'Femenino', 'Tejón Busto', 'alumno26', 'Estudiante'),
(36, 'Estudiante', '2004-04-20', 'alumno27', 'Mateo', 'Masculino', 'Vázquez Prado', 'alumno27', 'Estudiante'),
(37, 'Estudiante', '2002-06-10', 'alumno28', 'Lara', 'Femenino', 'Vega Salas', 'alumno28', 'Estudiante'),
(38, 'Estudiante', '2003-09-25', 'alumno29', 'Bruno', 'Masculino', 'Villa Campo', 'alumno29', 'Estudiante'),
(39, 'Estudiante', '2004-12-01', 'alumno30', 'Olivia', 'Femenino', 'Zapata Ferrera', 'alumno30', 'Estudiante'),
(40, 'Profesor', '1974-02-03', 'profesor01', 'Miguel', 'Masculino', 'Alonso Castañón', 'profesor01', 'Profesor/Tutor'),
(41, 'Profesor', '1981-06-15', 'profesor02', 'Patricia', 'Femenino', 'Álvarez Miranda', 'profesor02', 'Profesor/Tutor'),
(42, 'Profesor', '1979-09-29', 'profesor03', 'Roberto', 'Masculino', 'Arias Blanco', 'profesor03', 'Profesor/Tutor'),
(43, 'Profesor', '1984-01-11', 'profesor04', 'Beatriz', 'Femenino', 'Cano Peláez', 'profesor04', 'Profesor/Tutor'),
(44, 'Profesor', '1977-03-24', 'profesor05', 'Jorge', 'Masculino', 'Díaz Fernández', 'profesor05', 'Profesor/Tutor'),
(45, 'Profesor', '1980-07-08', 'profesor06', 'Cristina', 'Femenino', 'García Morán', 'profesor06', 'Profesor/Tutor'),
(46, 'Profesor', '1976-10-19', 'profesor07', 'Raúl', 'Masculino', 'González Rivas', 'profesor07', 'Profesor/Tutor'),
(47, 'Profesor', '1983-04-27', 'profesor08', 'Isabel', 'Femenino', 'López Serrano', 'profesor08', 'Profesor/Tutor'),
(48, 'Profesor', '1978-12-06', 'profesor09', 'Óscar', 'Masculino', 'Martínez Suárez', 'profesor09', 'Profesor/Tutor'),
(49, 'Profesor', '1982-08-31', 'profesor10', 'Silvia', 'Femenino', 'Pérez Vega', 'profesor10', 'Profesor/Tutor'),
(50, 'Tutor', '1986-02-18', 'tutor01', 'Raquel', 'Femenino', 'Santos Arias', 'tutor01', 'Tutor de Empresa'),
(51, 'Tutor', '1981-05-09', 'tutor02', 'Andrés', 'Masculino', 'Crespo Villa', 'tutor02', 'Tutor de Empresa'),
(52, 'Tutor', '1984-11-21', 'tutor03', 'Mónica', 'Femenino', 'Iglesias Pardo', 'tutor03', 'Tutor de Empresa'),
(53, 'Tutor', '1979-07-14', 'tutor04', 'David', 'Masculino', 'Méndez Castro', 'tutor04', 'Tutor de Empresa'),
(54, 'Tutor', '1987-03-05', 'tutor05', 'Teresa', 'Femenino', 'Llaneza Cuervo', 'tutor05', 'Tutor de Empresa'),
(55, 'Estudiante', '2003-02-06', 'alumno31', 'Gabriel', 'Masculino', 'Acosta Prendes', 'alumno31', 'Estudiante'),
(56, 'Estudiante', '2004-05-18', 'alumno32', 'Alba', 'Femenino', 'Barrio Iglesias', 'alumno32', 'Estudiante'),
(57, 'Estudiante', '2002-07-24', 'alumno33', 'Enol', 'Masculino', 'Castaño Merino', 'alumno33', 'Estudiante'),
(58, 'Estudiante', '2003-10-09', 'alumno34', 'Julia', 'Femenino', 'Cuervo Salcedo', 'alumno34', 'Estudiante'),
(59, 'Estudiante', '2004-01-13', 'alumno35', 'Lucas', 'Masculino', 'Delgado Arias', 'alumno35', 'Estudiante'),
(60, 'Estudiante', '2002-04-27', 'alumno36', 'Sofía', 'Femenino', 'Espina Villar', 'alumno36', 'Estudiante'),
(61, 'Estudiante', '2003-08-03', 'alumno37', 'Nicolás', 'Masculino', 'Fidalgo Cano', 'alumno37', 'Estudiante'),
(62, 'Estudiante', '2004-11-15', 'alumno38', 'Valeria', 'Femenino', 'Linares Moro', 'alumno38', 'Estudiante'),
(63, 'Estudiante', '2002-12-21', 'alumno39', 'Rodrigo', 'Masculino', 'Méndez Plaza', 'alumno39', 'Estudiante'),
(64, 'Estudiante', '2003-06-11', 'alumno40', 'Candela', 'Femenino', 'Quirós Ferrera', 'alumno40', 'Estudiante');

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `administrador`
--
ALTER TABLE `administrador`
  ADD CONSTRAINT `fk_administrador_user` FOREIGN KEY (`id`) REFERENCES `user` (`id`);

--
-- Filtros para la tabla `curso`
--
ALTER TABLE `curso`
  ADD CONSTRAINT `fk_curso_ciclo` FOREIGN KEY (`ciclo_formativo_id`) REFERENCES `ciclo_formativo` (`id_ciclo`);

--
-- Filtros para la tabla `documento`
--
ALTER TABLE `documento`
  ADD CONSTRAINT `fk_documento_formacion` FOREIGN KEY (`formacion_empresa_id`) REFERENCES `formacion_empresa` (`id_formacion`);

--
-- Filtros para la tabla `estudiante`
--
ALTER TABLE `estudiante`
  ADD CONSTRAINT `fk_estudiante_curso` FOREIGN KEY (`curso_id`) REFERENCES `curso` (`id_curso`),
  ADD CONSTRAINT `fk_estudiante_user` FOREIGN KEY (`id`) REFERENCES `user` (`id`);

--
-- Filtros para la tabla `formacion_empresa`
--
ALTER TABLE `formacion_empresa`
  ADD CONSTRAINT `fk_formacion_curso` FOREIGN KEY (`curso_id`) REFERENCES `curso` (`id_curso`),
  ADD CONSTRAINT `fk_formacion_estudiante` FOREIGN KEY (`estudiante_id`) REFERENCES `estudiante` (`id`),
  ADD CONSTRAINT `fk_formacion_profesor` FOREIGN KEY (`profesor_id`) REFERENCES `profesor` (`id`),
  ADD CONSTRAINT `fk_formacion_tutor` FOREIGN KEY (`tutor_id`) REFERENCES `tutor` (`id`);

--
-- Filtros para la tabla `modulo`
--
ALTER TABLE `modulo`
  ADD CONSTRAINT `fk_modulo_curso` FOREIGN KEY (`curso_id`) REFERENCES `curso` (`id_curso`),
  ADD CONSTRAINT `fk_modulo_profesor` FOREIGN KEY (`profesor_id`) REFERENCES `profesor` (`id`);

--
-- Filtros para la tabla `profesor`
--
ALTER TABLE `profesor`
  ADD CONSTRAINT `fk_profesor_user` FOREIGN KEY (`id`) REFERENCES `user` (`id`);

--
-- Filtros para la tabla `tutor`
--
ALTER TABLE `tutor`
  ADD CONSTRAINT `fk_tutor_empresa` FOREIGN KEY (`empresa_id`) REFERENCES `empresa` (`id_empresa`),
  ADD CONSTRAINT `fk_tutor_user` FOREIGN KEY (`id`) REFERENCES `user` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
