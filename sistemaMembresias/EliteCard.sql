-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 08-08-2024 a las 22:15:04
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
-- Base de datos: `elitecardproject`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `beneficio`
--

CREATE TABLE `beneficio` (
  `codigo` varchar(5) NOT NULL,
  `descripcion` varchar(50) NOT NULL,
  `cantidad` int(8) NOT NULL,
  `nivelTarjeta` varchar(3) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `beneficio`
--

INSERT INTO `beneficio` (`codigo`, `descripcion`, `cantidad`, `nivelTarjeta`) VALUES
('DPNV1', 'Descuento permanente', 5, 'NV1'),
('DPNV2', 'Descuento permanente', 10, 'NV2'),
('DPNV3', 'Descuento permanente', 15, 'NV3'),
('GPNV1', 'Ganancia puntos', 10, 'NV1'),
('GPNV2', 'Ganancia puntos', 20, 'NV2'),
('GPNV3', 'Ganancia puntos', 30, 'NV3'),
('LPNV1', 'Limite de puntos', 300, 'NV1'),
('LPNV2', 'Limite de puntos', 500, 'NV2'),
('LPNV3', 'Limite de puntos', 1000, 'NV3'),
('VPNV1', 'Valor de un punto', 50, 'NV1'),
('VPNV2', 'Valor de un punto', 50, 'NV2'),
('VPNV3', 'Valor de un punto', 50, 'NV3');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `cancelacion`
--

CREATE TABLE `cancelacion` (
  `numero` int(10) NOT NULL,
  `fechaCancelacion` date NOT NULL,
  `motivo` varchar(60) NOT NULL,
  `tarjetamemb` int(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `cancelacion`
--

INSERT INTO `cancelacion` (`numero`, `fechaCancelacion`, `motivo`, `tarjetamemb`) VALUES
(10, '2024-03-03', 'No desea continuar con el servicio', 10009),
(11, '2024-08-04', 'El servicio es demasiado bueno', 10019),
(12, '2024-08-07', 'Abarrotes Enjambre esta lejos del cliente.', 10024),
(13, '2024-08-07', 'Los beneficios son demasiado buenos', 10025);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `empleado`
--

CREATE TABLE `empleado` (
  `numero` int(5) NOT NULL,
  `correo` varchar(35) DEFAULT NULL,
  `contrasenia` varchar(15) NOT NULL,
  `nombre` varchar(30) NOT NULL,
  `primerApellido` varchar(30) NOT NULL,
  `segundoApellido` varchar(30) DEFAULT NULL,
  `puesto` varchar(3) NOT NULL,
  `estado` tinyint(1) NOT NULL DEFAULT 1,
  `establecimiento` varchar(3) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `empleado`
--

INSERT INTO `empleado` (`numero`, `correo`, `contrasenia`, `nombre`, `primerApellido`, `segundoApellido`, `puesto`, `estado`, `establecimiento`) VALUES
(1, 'juanp@gmail.com', 'cisco', 'Juan', 'Pérez', 'González', 'PT1', 0, 'EST'),
(2, 'mary@outlook.com', 'contrasenia', 'María', 'López', 'Rodríguez', 'PT2', 1, 'EST'),
(3, 'pedropedrope@gmail.com', 'class', 'Pedro', 'García', 'Hernández', 'PT3', 1, 'EST'),
(4, 'almejandro@gmail.com', '123456', 'Alejandro', 'Gómez', 'Martínez', 'PT4', 1, 'EST'),
(5, 'luisito@hotmail.com', 'ciscopher', 'Luis', 'Sánchez', 'Gómez', 'PT1', 1, 'EST'),
(6, 'anasofi@example.com', 'roblox', 'Ana Sofía', 'Romero', 'Fernández', 'PT2', 1, 'EST'),
(7, 'oscarso@gmail.com', '123456', 'Oscar Gael', 'Soto', 'Garcia', 'PT4', 1, 'EST'),
(8, 'lucca@gmail.com', '123456', 'Lucas', 'Dominguez', 'Vallarta', 'PT1', 1, 'EST'),
(9, 'alc@gmail.com', '123456', 'Angel', 'Alcantara', 'Huerta', 'PT4', 1, 'EST'),
(10, 'danigg@gmail.com', 'cisco', 'Daniel', 'Garcia', 'Garfias', 'PT3', 1, 'EST'),
(11, 'cristo@gmail.com', 'cisco', 'Christopher', 'Gonzalez', 'Leyva', 'PT2', 1, 'EST'),
(12, 'arielito@gmail.com', '123456', 'Ariel', 'Torres', 'Iniguez', 'PT2', 1, 'EST');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `establecimiento`
--

CREATE TABLE `establecimiento` (
  `codigo` varchar(3) NOT NULL,
  `nombre` varchar(25) NOT NULL,
  `calle` varchar(25) NOT NULL,
  `numero` varchar(5) NOT NULL,
  `colonia` varchar(20) NOT NULL,
  `ciudad` varchar(20) NOT NULL,
  `codigoPostal` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `establecimiento`
--

INSERT INTO `establecimiento` (`codigo`, `nombre`, `calle`, `numero`, `colonia`, `ciudad`, `codigoPostal`) VALUES
('EST', 'Abarrotes Enjambre', 'Av. Principal', '123', 'Centro', 'Tijuana', '22236');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `miembro`
--

CREATE TABLE `miembro` (
  `numero` int(8) NOT NULL,
  `fechaRegistro` date NOT NULL,
  `numTel` varchar(15) NOT NULL,
  `nombre` varchar(30) NOT NULL,
  `primerApellido` varchar(30) NOT NULL,
  `segundoApellido` varchar(30) DEFAULT NULL,
  `estado` tinyint(1) NOT NULL DEFAULT 1,
  `correo` varchar(45) DEFAULT NULL,
  `empleado` int(5) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `miembro`
--

INSERT INTO `miembro` (`numero`, `fechaRegistro`, `numTel`, `nombre`, `primerApellido`, `segundoApellido`, `estado`, `correo`, `empleado`) VALUES
(1000, '2019-01-01', '6641123255', 'Ricardo', 'Pérez', 'Sevilla', 1, 'richie@gmail.com', 1),
(1001, '2019-01-05', '6647899212', 'José Luis', 'Slobotzky', 'Arteaga', 1, 'marialop@gmail.com', 2),
(1002, '2020-01-10', '6612342341', 'Bernardo', 'Villalobos', 'Hernández', 1, 'ingpedro@example.com', 3),
(1003, '2020-06-15', '6639082723', 'Patricia', 'Díaz', 'Cervantes', 1, 'patricia@gmail.com', 4),
(1004, '2021-02-01', '6646723213', 'Javier', 'Gómez', 'Daniel', 1, 'danieljj@gmail.com', 5),
(1005, '2021-08-05', '6667778889', 'Daniela', 'Tiburcio', 'García', 1, 'garc@gmail.com', 6),
(1006, '2022-02-10', '6645655645', 'Laura', 'Guzmán', 'Ramírez', 1, 'laura@gmail.com', 1),
(1007, '2022-09-15', '6645970121', 'Mateo', 'González', 'López', 1, 'matt@gmail.com', 2),
(1008, '2023-03-01', '6645632344', 'Valentina', 'Martínez', 'García', 1, 'valentinael@outlook.com', 3),
(1009, '2024-03-05', '661234567', 'Diego', 'Rodríguez', 'Díaz', 1, 'diegogo@gmail.com', 4),
(1010, '2024-08-02', '6644879993', 'Oscar Gael', 'Soto', 'Garcia', 1, 'oscargael@gmail.com', 4),
(1011, '2024-08-02', '6644787765', 'Daniel', 'Martinez', 'Bustamante', 1, 'dani@gmail.com', 6),
(1012, '2024-08-02', '6645628878', 'Cristopher', 'Gonzale', 'Leyva', 0, 'cristo@gmail.com', 1),
(1013, '2024-08-02', '6645671123', 'Carlos', 'Moreno', 'Villa', 1, 'mvq@gmail.com', 1),
(1014, '2024-08-02', '6644879993', 'Edson', 'Alvarez', 'Perez', 1, 'edson@gmail.com', 4),
(1015, '2024-08-02', '6645671123', 'Jesus', 'Tiburcio', 'Gonzalez', 1, 'tibu@gmail.com', 4),
(1016, '2024-08-02', '6645289998', 'Abraham', 'Tiburcio', 'Gonzalez', 1, 'tibu2@gmail.com', 4),
(1017, '2024-08-02', '6645676654', 'Diana', 'Martinez', 'Perez', 1, 'diana@gmail.com', 4),
(1018, '2024-08-02', '6641278755', 'Marcos', 'Ramirez', 'Navarro', 1, 'mn@gmail.com', 2),
(1019, '2024-08-02', '6641289822', 'Alan', 'Medina', 'Becerra', 0, 'alan@gmail.com', 3),
(1020, '2024-08-02', '6644789936', 'Luis', 'Diaz', 'Reyes', 1, 'luy@gmail.com', 4),
(1021, '2024-08-02', '6642341123', 'Daniel', 'Mendez', 'Perez', 1, 'mpd@gmail.com', 4),
(1022, '2024-08-02', '6645323345', 'Kevin', 'Alvarez', 'Moreno', 1, 'kevinalvarezz@gmail.com', 1),
(1023, '2024-08-02', '6647879989', 'Alvaro', 'Fidalgo', 'Huescas', 1, 'maguito@gmail.com', 5),
(1024, '2024-08-02', '66452345553', 'Henry Martin', 'Mex', 'Lopez', 1, 'labomba@gmail.com', 4),
(1025, '2024-08-02', '6648790992', 'Ramon', 'Juarez', 'Tirado', 1, 'ramon@gmail.com', 4),
(1026, '2024-08-03', 'null', 'Illian', 'Hernandez', 'Zapata', 1, 'illian@gmail.com', 4),
(1027, '2024-08-03', 'null', 'Cristian', 'Calderon', '', 1, 'chicote@gmail.com', 4),
(1028, '2024-08-03', 'null', 'Jose', 'Candela', 'Alvarez', 1, 'canalv@gmail.com', 4),
(1029, '2024-08-04', 'null', 'Ariel', 'Torres', 'Iniguez', 1, 'arielito@gmail.com', 4),
(1030, '2024-08-04', 'null', 'Brian', 'Becerra', 'Sanchez', 1, 'goat@gmail.com', 2),
(1031, '2024-08-04', 'null', 'Angel', 'Gomez', 'Daniel', 1, 'dani@gmail.com', 2),
(1032, '2024-08-04', 'null', 'Vladimir', 'Herrera', 'Partida', 1, 'vladi@gmail.com', 4),
(1033, '2024-08-04', '6645456676', 'Jared', 'Garcia', 'Manga', 1, 'jared@gmail.com', 4),
(1034, '2024-08-04', '6645657787', 'Alan', 'Mozo', 'Barreras', 1, 'mozo@gmail.com', 4),
(1035, '2024-08-04', '6645456676', 'Josue', 'Lara', 'Lopez', 1, 'Lara@gmail.com', 4),
(1036, '2024-08-04', '6645456676', 'Javier', 'Hernandez', 'Bautista', 1, 'chicharito@gmail.com', 4),
(1037, '2024-08-04', '6645638878', 'Javier', 'Franco', 'Escamilla', 1, 'franco@gmail.com', 4),
(1038, '2024-08-04', '6645478898', 'Christian', 'Meza', 'Perez', 1, 'chris@gmail.com', 4),
(1039, '2024-08-04', '6645487783', 'Raul', 'Vazquez', 'Vazquez', 1, 'rv@gmail.com', 4),
(1040, '2024-08-04', '6645328878', 'Jaime', 'Lozano', 'Rodriguez', 1, 'jl@gmail.com', 4),
(1041, '2024-08-05', '6647768798', 'Gabriel', 'Guzman', 'Vazquez', 1, 'gabo@gmail.com', 4),
(1042, '2024-08-07', '6641234567', 'Cesar', 'Quintana', 'Martinez', 1, 'Cesar@ut-tijuana.edu.mx', 4),
(1043, '2024-08-07', '6645456632', 'Juan', 'Lopez', 'Alcaraz', 1, 'juan@gmail.com', 4),
(1044, '2024-08-07', '6644562232', 'Fabricio', 'Romero', 'Lukaku', 1, '', 4);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `niveltarjeta`
--

CREATE TABLE `niveltarjeta` (
  `codigo` varchar(3) NOT NULL,
  `nombre` varchar(20) NOT NULL,
  `descripcion` varchar(50) DEFAULT NULL,
  `costo` decimal(8,2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `niveltarjeta`
--

INSERT INTO `niveltarjeta` (`codigo`, `nombre`, `descripcion`, `costo`) VALUES
('NV1', 'Bronce', 'Nivel básico', 500.00),
('NV2', 'Plata', 'Nivel intermedio', 650.00),
('NV3', 'Oro', 'Nivel premium', 1100.00);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `puesto`
--

CREATE TABLE `puesto` (
  `codigo` varchar(3) NOT NULL,
  `nombre` varchar(25) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `puesto`
--

INSERT INTO `puesto` (`codigo`, `nombre`) VALUES
('PT1', 'Gerente'),
('PT2', 'Vendedor'),
('PT3', 'Cajero'),
('PT4', 'Administrativo');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `renovacion`
--

CREATE TABLE `renovacion` (
  `numero` int(8) NOT NULL,
  `fechaRenovacion` date NOT NULL,
  `monto` decimal(10,2) NOT NULL,
  `tarjetaMemb` int(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `renovacion`
--

INSERT INTO `renovacion` (`numero`, `fechaRenovacion`, `monto`, `tarjetaMemb`) VALUES
(100, '2021-10-10', 500.00, 10000),
(101, '2022-11-12', 500.00, 10000),
(102, '2023-12-01', 500.00, 10000),
(103, '2021-04-20', 500.00, 10001),
(104, '2023-08-05', 500.00, 10001),
(105, '2023-09-15', 500.00, 10003),
(106, '2023-03-15', 1500.00, 10005),
(107, '2023-01-21', 500.00, 10006),
(108, '2023-10-12', 1000.00, 10007),
(109, '2024-03-12', 1500.00, 10008),
(110, '2024-08-02', 500.00, 10000),
(111, '2024-08-03', 1100.00, 10018),
(112, '2024-08-03', 500.00, 10000),
(113, '2024-08-04', 500.00, 10001),
(114, '2024-08-04', 500.00, 10000),
(115, '2024-08-04', 500.00, 10000),
(116, '2024-08-04', 500.00, 10000),
(117, '2024-08-04', 500.00, 10000),
(118, '2024-08-04', 500.00, 10000),
(119, '2024-08-07', 500.00, 10000);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `tarjetamemb`
--

CREATE TABLE `tarjetamemb` (
  `numero` int(10) NOT NULL,
  `fechaCreacion` date NOT NULL,
  `fechaExpiracion` date NOT NULL,
  `estatus` tinyint(1) NOT NULL,
  `puntosActuales` int(8) NOT NULL,
  `puntosAcumulados` int(10) NOT NULL,
  `empleado` int(5) NOT NULL,
  `miembro` int(8) NOT NULL,
  `nivelTarjeta` varchar(3) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `tarjetamemb`
--

INSERT INTO `tarjetamemb` (`numero`, `fechaCreacion`, `fechaExpiracion`, `estatus`, `puntosActuales`, `puntosAcumulados`, `empleado`, `miembro`, `nivelTarjeta`) VALUES
(10000, '2019-01-01', '2033-07-31', 1, 68, 821, 1, 1000, 'NV1'),
(10001, '2019-01-05', '2025-08-04', 1, 43, 1234, 2, 1001, 'NV1'),
(10002, '2020-01-10', '2026-01-10', 1, 105, 1296, 3, 1002, 'NV2'),
(10003, '2020-06-15', '2026-06-15', 1, 100, 2345, 4, 1003, 'NV1'),
(10004, '2021-02-01', '2027-02-01', 1, 65, 67, 5, 1004, 'NV2'),
(10005, '2021-08-05', '2027-08-05', 1, 62, 739, 6, 1005, 'NV3'),
(10006, '2022-02-10', '2028-02-10', 1, 11, 98, 1, 1006, 'NV1'),
(10007, '2022-09-15', '2028-09-15', 1, 0, 87, 2, 1007, 'NV2'),
(10008, '2023-03-01', '2029-03-01', 1, 74, 701, 3, 1008, 'NV3'),
(10009, '2024-03-05', '2030-03-05', 1, 44, 567, 4, 1009, 'NV1'),
(10010, '2024-07-28', '2030-07-28', 1, 169, 292, 3, 1002, 'NV3'),
(10011, '2024-08-02', '2025-08-02', 1, 114, 0, 4, 1019, 'NV3'),
(10012, '2024-08-02', '2025-08-02', 1, 73, 310, 4, 1020, 'NV3'),
(10013, '2024-08-02', '2025-08-02', 1, 0, 0, 4, 1022, 'NV3'),
(10014, '2024-08-02', '2025-08-02', 1, 25, 0, 4, 1023, 'NV3'),
(10015, '2024-08-02', '2025-08-02', 1, 0, 0, 4, 1024, 'NV1'),
(10016, '2024-08-02', '2025-08-02', 1, 0, 0, 4, 1025, 'NV2'),
(10017, '2024-08-03', '2025-08-03', 1, 0, 0, 4, 1026, 'NV3'),
(10018, '2024-08-03', '2026-08-03', 1, 114, 174, 4, 1027, 'NV3'),
(10019, '2024-08-04', '2024-08-04', 0, 0, 0, 4, 1029, 'NV3'),
(10020, '2024-08-04', '2025-08-04', 1, 46, 56, 2, 1030, 'NV1'),
(10021, '2024-08-04', '2025-08-04', 1, 0, 0, 4, 1033, 'NV3'),
(10022, '2024-08-05', '2025-08-05', 1, 0, 0, 4, 1041, 'NV3'),
(10023, '2024-08-07', '2025-08-07', 1, 0, 0, 4, 1042, 'NV3'),
(10024, '2024-08-07', '2024-08-07', 0, 0, 0, 4, 1000, 'NV1'),
(10025, '2024-08-07', '2024-08-07', 0, 0, 0, 4, 1000, 'NV3'),
(10026, '2024-08-07', '2025-08-07', 1, 0, 0, 4, 1043, 'NV2');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `venta`
--

CREATE TABLE `venta` (
  `numero` int(10) NOT NULL,
  `fecha` date NOT NULL,
  `total` decimal(10,2) NOT NULL,
  `puntosUsados` int(8) NOT NULL,
  `puntosGanados` int(8) NOT NULL,
  `totalBeneficio` decimal(10,2) NOT NULL,
  `IVA` decimal(10,2) NOT NULL,
  `subtotalBeneficio` decimal(10,2) NOT NULL,
  `tarjetaMemb` int(10) NOT NULL,
  `nivelTarjeta` varchar(3) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `venta`
--

INSERT INTO `venta` (`numero`, `fecha`, `total`, `puntosUsados`, `puntosGanados`, `totalBeneficio`, `IVA`, `subtotalBeneficio`, `tarjetaMemb`, `nivelTarjeta`) VALUES
(1000000, '2019-01-02', 130.00, 40, 13, 100.00, 16.00, 74.00, 10000, 'NV1'),
(1000001, '2019-01-07', 200.00, 0, 20, 180.00, 32.00, 148.00, 10001, 'NV1'),
(1000002, '2020-01-12', 300.00, 0, 60, 270.00, 48.00, 222.00, 10002, 'NV2'),
(1000003, '2020-01-17', 400.00, 0, 40, 360.00, 64.00, 296.00, 10003, 'NV1'),
(1000004, '2021-01-22', 500.00, 12, 100, 450.00, 80.00, 370.00, 10004, 'NV2'),
(1000005, '2021-01-27', 600.00, 0, 180, 540.00, 96.00, 444.00, 10005, 'NV3'),
(1000006, '2021-02-01', 700.00, 0, 70, 630.00, 112.00, 518.00, 10006, 'NV1'),
(1000007, '2022-02-06', 800.00, 20, 160, 720.00, 128.00, 592.00, 10007, 'NV2'),
(1000008, '2022-02-11', 900.00, 0, 270, 810.00, 144.00, 666.00, 10008, 'NV3'),
(1000009, '2023-02-16', 1000.00, 0, 100, 900.00, 160.00, 740.00, 10009, 'NV1'),
(1000010, '2024-02-21', 1100.00, 0, 110, 990.00, 176.00, 814.00, 10001, 'NV1'),
(1000011, '2024-02-26', 1200.00, 60, 120, 1080.00, 192.00, 888.00, 10001, 'NV1'),
(1000012, '2024-03-03', 1300.00, 9, 130, 1170.00, 208.00, 962.00, 10002, 'NV2'),
(1000013, '2024-03-08', 1400.00, 30, 140, 1260.00, 224.00, 1036.00, 10003, 'NV1'),
(1000014, '2024-08-02', 100.00, 9, 17, 85.50, 13.68, 71.82, 10002, 'NV2'),
(1000015, '2024-08-02', 200.00, 0, 51, 170.00, 27.20, 142.80, 10012, 'NV3'),
(1000016, '2024-08-02', 100.00, 1, 25, 84.50, 13.52, 70.98, 10012, 'NV3'),
(1000017, '2024-08-02', 20.00, 5, 4, 14.50, 2.32, 12.18, 10012, 'NV3'),
(1000018, '2024-08-02', 100.00, 7, 9, 91.50, 14.64, 76.86, 10000, 'NV1'),
(1000019, '2024-08-02', 100.00, 9, 9, 90.50, 14.48, 76.02, 10000, 'NV1'),
(1000020, '2024-08-02', 250.00, 4, 63, 210.50, 33.68, 176.82, 10012, 'NV3'),
(1000021, '2024-08-02', 600.00, 100, 138, 460.00, 73.60, 386.40, 10010, 'NV3'),
(1000022, '2024-08-02', 1000.00, 100, 240, 800.00, 128.00, 672.00, 10012, 'NV3'),
(1000023, '2024-08-02', 200.00, 50, 43, 145.00, 23.20, 121.80, 10005, 'NV3'),
(1000024, '2024-08-02', 300.00, 12, 74, 249.00, 39.84, 209.16, 10008, 'NV3'),
(1000025, '2024-08-02', 450.00, 0, 114, 382.50, 61.20, 321.30, 10011, 'NV3'),
(1000026, '2024-08-02', 100.00, 0, 25, 85.00, 13.60, 71.40, 10014, 'NV3'),
(1000027, '2024-08-03', 100.00, 200, 0, 0.00, 0.00, 0.00, 10012, 'NV3'),
(1000028, '2024-08-03', 100.00, 0, 25, 85.00, 13.60, 71.40, 10018, 'NV3'),
(1000029, '2024-08-03', 100.00, 20, 22, 75.00, 12.00, 63.00, 10018, 'NV3'),
(1000030, '2024-08-03', 200.00, 27, 46, 156.50, 25.04, 131.46, 10018, 'NV3'),
(1000031, '2024-08-03', 450.00, 40, 108, 362.50, 58.00, 304.50, 10018, 'NV3'),
(1000032, '2024-08-03', 300.00, 47, 69, 231.50, 37.04, 194.46, 10010, 'NV3'),
(1000033, '2024-08-04', 100.00, 7, 17, 86.50, 13.84, 72.66, 10002, 'NV2'),
(1000034, '2024-08-04', 300.00, 70, 25, 250.00, 40.00, 210.00, 10000, 'NV1'),
(1000035, '2024-08-05', 500.00, 30, 46, 460.00, 73.60, 386.40, 10000, 'NV1'),
(1000036, '2024-08-05', 1000.00, 50, 92, 925.00, 148.00, 777.00, 10000, 'NV1'),
(1000037, '2024-08-05', 200.00, 10, 18, 185.00, 29.60, 155.40, 10000, 'NV1'),
(1000038, '2024-08-06', 200.00, 0, 19, 190.00, 30.40, 159.60, 10020, 'NV1'),
(1000039, '2024-08-07', 100.00, 50, 7, 70.00, 11.20, 58.80, 10000, 'NV1'),
(1000040, '2024-08-07', 400.00, 10, 37, 375.00, 60.00, 315.00, 10020, 'NV1'),
(1000041, '2024-08-07', 200.00, 7, 18, 186.50, 29.84, 156.66, 10000, 'NV1'),
(1000042, '2024-08-08', 200.00, 7, 35, 176.50, 28.24, 148.26, 10002, 'NV2');

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `beneficio`
--
ALTER TABLE `beneficio`
  ADD PRIMARY KEY (`codigo`),
  ADD KEY `nivelTarjeta` (`nivelTarjeta`);

--
-- Indices de la tabla `cancelacion`
--
ALTER TABLE `cancelacion`
  ADD PRIMARY KEY (`numero`),
  ADD KEY `tarjetamemb` (`tarjetamemb`);

--
-- Indices de la tabla `empleado`
--
ALTER TABLE `empleado`
  ADD PRIMARY KEY (`numero`),
  ADD KEY `puesto` (`puesto`),
  ADD KEY `establecimiento` (`establecimiento`);

--
-- Indices de la tabla `establecimiento`
--
ALTER TABLE `establecimiento`
  ADD PRIMARY KEY (`codigo`);

--
-- Indices de la tabla `miembro`
--
ALTER TABLE `miembro`
  ADD PRIMARY KEY (`numero`),
  ADD KEY `empleado` (`empleado`);

--
-- Indices de la tabla `niveltarjeta`
--
ALTER TABLE `niveltarjeta`
  ADD PRIMARY KEY (`codigo`);

--
-- Indices de la tabla `puesto`
--
ALTER TABLE `puesto`
  ADD PRIMARY KEY (`codigo`);

--
-- Indices de la tabla `renovacion`
--
ALTER TABLE `renovacion`
  ADD PRIMARY KEY (`numero`),
  ADD KEY `tarjetaMemb` (`tarjetaMemb`);

--
-- Indices de la tabla `tarjetamemb`
--
ALTER TABLE `tarjetamemb`
  ADD PRIMARY KEY (`numero`),
  ADD KEY `empleado` (`empleado`),
  ADD KEY `miembro` (`miembro`),
  ADD KEY `nivelTarjeta` (`nivelTarjeta`);

--
-- Indices de la tabla `venta`
--
ALTER TABLE `venta`
  ADD PRIMARY KEY (`numero`),
  ADD KEY `tarjetaMemb` (`tarjetaMemb`),
  ADD KEY `nivelTarjeta` (`nivelTarjeta`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `cancelacion`
--
ALTER TABLE `cancelacion`
  MODIFY `numero` int(10) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=14;

--
-- AUTO_INCREMENT de la tabla `empleado`
--
ALTER TABLE `empleado`
  MODIFY `numero` int(5) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=13;

--
-- AUTO_INCREMENT de la tabla `miembro`
--
ALTER TABLE `miembro`
  MODIFY `numero` int(8) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=1045;

--
-- AUTO_INCREMENT de la tabla `renovacion`
--
ALTER TABLE `renovacion`
  MODIFY `numero` int(8) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=120;

--
-- AUTO_INCREMENT de la tabla `tarjetamemb`
--
ALTER TABLE `tarjetamemb`
  MODIFY `numero` int(10) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10027;

--
-- AUTO_INCREMENT de la tabla `venta`
--
ALTER TABLE `venta`
  MODIFY `numero` int(10) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=1000043;

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `beneficio`
--
ALTER TABLE `beneficio`
  ADD CONSTRAINT `beneficio_ibfk_1` FOREIGN KEY (`nivelTarjeta`) REFERENCES `niveltarjeta` (`codigo`);

--
-- Filtros para la tabla `cancelacion`
--
ALTER TABLE `cancelacion`
  ADD CONSTRAINT `cancelacion_ibfk_1` FOREIGN KEY (`tarjetamemb`) REFERENCES `tarjetamemb` (`numero`);

--
-- Filtros para la tabla `empleado`
--
ALTER TABLE `empleado`
  ADD CONSTRAINT `empleado_ibfk_1` FOREIGN KEY (`puesto`) REFERENCES `puesto` (`codigo`),
  ADD CONSTRAINT `empleado_ibfk_2` FOREIGN KEY (`establecimiento`) REFERENCES `establecimiento` (`codigo`);

--
-- Filtros para la tabla `miembro`
--
ALTER TABLE `miembro`
  ADD CONSTRAINT `miembro_ibfk_1` FOREIGN KEY (`empleado`) REFERENCES `empleado` (`numero`);

--
-- Filtros para la tabla `renovacion`
--
ALTER TABLE `renovacion`
  ADD CONSTRAINT `renovacion_ibfk_1` FOREIGN KEY (`tarjetaMemb`) REFERENCES `tarjetamemb` (`numero`);

--
-- Filtros para la tabla `tarjetamemb`
--
ALTER TABLE `tarjetamemb`
  ADD CONSTRAINT `tarjetamemb_ibfk_1` FOREIGN KEY (`empleado`) REFERENCES `empleado` (`numero`),
  ADD CONSTRAINT `tarjetamemb_ibfk_2` FOREIGN KEY (`miembro`) REFERENCES `miembro` (`numero`),
  ADD CONSTRAINT `tarjetamemb_ibfk_3` FOREIGN KEY (`nivelTarjeta`) REFERENCES `niveltarjeta` (`codigo`);

--
-- Filtros para la tabla `venta`
--
ALTER TABLE `venta`
  ADD CONSTRAINT `venta_ibfk_1` FOREIGN KEY (`tarjetaMemb`) REFERENCES `tarjetamemb` (`numero`),
  ADD CONSTRAINT `venta_ibfk_2` FOREIGN KEY (`nivelTarjeta`) REFERENCES `niveltarjeta` (`codigo`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
