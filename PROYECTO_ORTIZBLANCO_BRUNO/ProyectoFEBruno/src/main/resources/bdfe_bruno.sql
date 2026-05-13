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
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `empresa`
--

INSERT INTO `empresa` (`id_empresa`, `nombre`, `direccion`) VALUES
(1, 'Empresa Tecnológica S.L.', 'Calle Principal 10, Gijón'),
(2, 'Desarrollo Digital S.A.', 'Avenida de la Constitución 25, Oviedo'),
(3, 'AsturCode Solutions S.L.', 'Calle Uría 18, Oviedo'),
(4, 'NorteData Innovación S.A.', 'Parque Tecnológico 7, Gijón');

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
(7, NULL);

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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

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
(5, 0);

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
(9, '684333444', 4);

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
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

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
(9, 'Tutor', '1988-01-01', 'diego.tutor', 'Diego', 'Masculino', 'Suárez Blanco', 'diego123', 'Tutor de Empresa');

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
