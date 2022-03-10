-- phpMyAdmin SQL Dump
-- version 4.6.4
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 10-10-2019 a las 15:31:59
-- Versión del servidor: 5.7.14
-- Versión de PHP: 5.6.25

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `features`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `config`
--

CREATE TABLE `config` (
  `id` int(11) NOT NULL,
  `name` varchar(100) NOT NULL,
  `value` varchar(100) NOT NULL,
  `comments` varchar(250) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Volcado de datos para la tabla `config`
--

INSERT INTO `config` (`id`, `name`, `value`, `comments`) VALUES
(1, 'drawing', 'text', 'text/GraphViz/abego  http://treelayout.sourceforge.net/');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `constraints`
--

CREATE TABLE `constraints` (
  `idSystem` int(11) NOT NULL,
  `type` varchar(10) NOT NULL DEFAULT 'REQUIRE',
  `idFeature1` int(11) NOT NULL,
  `idFeature2` int(11) NOT NULL,
  `obs` varchar(200) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Volcado de datos para la tabla `constraints`
--

INSERT INTO `constraints` (`idSystem`, `type`, `idFeature1`, `idFeature2`, `obs`) VALUES
(1, 'REQUIRE', 9, 4, '');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `features`
--

CREATE TABLE `features` (
  `id` int(11) NOT NULL,
  `name` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Volcado de datos para la tabla `features`
--

INSERT INTO `features` (`id`, `name`) VALUES
(0, 'fx'),
(1, 'Home'),
(2, 'Security'),
(3, 'Credit Card'),
(4, 'Fingerprint'),
(5, 'Password'),
(6, 'Multimedia'),
(7, 'MM Basic'),
(8, 'MM Top'),
(9, 'MM Premium'),
(10, 'R'),
(11, 'f1'),
(12, 'f2'),
(13, 'f3'),
(14, 'f4'),
(15, 'f5'),
(16, 'f6'),
(17, 'f7'),
(18, 'fx (case 1)'),
(19, 'fx (case 2)'),
(20, 'fx (case 3)'),
(21, 'fx (case 4)'),
(22, 'fx (case 5)');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `featuressupertypes`
--

CREATE TABLE `featuressupertypes` (
  `id` int(11) NOT NULL,
  `idFeature` int(11) NOT NULL,
  `idSupertype` int(11) NOT NULL,
  `priority` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Volcado de datos para la tabla `featuressupertypes`
--

INSERT INTO `featuressupertypes` (`id`, `idFeature`, `idSupertype`, `priority`) VALUES
(6, 19, 2, 1),
(7, 20, 2, 1),
(8, 21, 1, 1),
(9, 21, 2, 2),
(10, 21, 5, 3),
(14, 11, 1, 1),
(15, 12, 2, 1),
(16, 13, 3, 1),
(17, 14, 4, 1),
(18, 15, 5, 1);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `products`
--

CREATE TABLE `products` (
  `idNode` int(11) NOT NULL,
  `idFeature` int(11) NOT NULL,
  `idParentNode` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `projects`
--

CREATE TABLE `projects` (
  `id` int(11) NOT NULL,
  `name` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Volcado de datos para la tabla `projects`
--

INSERT INTO `projects` (`id`, `name`) VALUES
(1, 'Smart Home'),
(2, 'Case 1'),
(3, 'case 2'),
(4, 'case 3'),
(5, 'case 4');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `supertypes`
--

CREATE TABLE `supertypes` (
  `id` int(11) NOT NULL,
  `name` varchar(100) NOT NULL,
  `obs` varchar(200) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Volcado de datos para la tabla `supertypes`
--

INSERT INTO `supertypes` (`id`, `name`, `obs`) VALUES
(1, 'STA', ''),
(2, 'STB', ''),
(3, 'STC', ''),
(4, 'STD', ''),
(5, 'STE', '');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `systems`
--

CREATE TABLE `systems` (
  `id` int(11) NOT NULL,
  `name` varchar(100) NOT NULL,
  `obs` varchar(250) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `system_tree`
--

CREATE TABLE `system_tree` (
  `id` int(11) NOT NULL,
  `idSystem` int(11) NOT NULL,
  `idNode` int(11) NOT NULL,
  `idParentNode` int(11) NOT NULL,
  `name` varchar(100) NOT NULL,
  `idFeature` int(11) NOT NULL,
  `nameFeature` varchar(100) NOT NULL,
  `relation` varchar(20) DEFAULT 'OR',
  `requirement` varchar(10) DEFAULT 'O',
  `obs` varchar(200) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Volcado de datos para la tabla `system_tree`
--

INSERT INTO `system_tree` (`id`, `idSystem`, `idNode`, `idParentNode`, `name`, `idFeature`, `nameFeature`, `relation`, `requirement`, `obs`) VALUES
(1, 1, 1, 0, 'Home', 1, 'Home', 'AND', 'MANDATORY', ''),
(2, 1, 2, 1, 'Security', 2, 'Security', 'OR', 'MANDATORY', ''),
(3, 1, 3, 2, 'Credit Card', 3, 'Credit Card', '', 'OPTIONAL', ''),
(4, 1, 4, 2, 'Fingerprint', 4, 'Fingerprint', '', 'OPTIONAL', ''),
(5, 1, 5, 2, 'Password', 5, 'Password', '', 'OPTIONAL', ''),
(6, 1, 6, 1, 'Multimedia', 6, 'Multimedia', 'XOR', 'MANDATORY', ''),
(7, 1, 7, 6, 'MM Basic', 7, 'MM Basic', '', 'OPTIONAL', ''),
(8, 1, 8, 6, 'MM Top', 8, 'MM Top', '', 'OPTIONAL', ''),
(9, 1, 9, 6, 'MM Premium', 9, 'MM Premium', '', 'OPTIONAL', ''),
(10, 2, 1, 0, 'CASE 1', 10, 'CASE 1', 'AND', 'MANDATORY', ''),
(11, 2, 2, 1, 'f1', 11, 'f1', 'AND', 'MANDATORY', ''),
(12, 2, 3, 1, 'f2', 12, 'f2', 'ALTERNATIVE', 'MANDATORY', ''),
(13, 2, 4, 3, 'f3', 13, 'f3', 'OR', 'OPTIONAL', ''),
(14, 2, 5, 3, 'f4', 14, 'f4', 'OR', 'OPTIONAL', ''),
(15, 3, 1, 0, 'CASE 2', 10, 'CASE 2', 'AND', 'MANDATORY', ''),
(16, 3, 2, 1, 'f1', 11, 'f1', 'AND', 'MANDATORY', ''),
(17, 3, 3, 1, 'f2', 12, 'f2', 'AND', 'MANDATORY', ''),
(18, 4, 1, 0, 'CASE 3', 10, 'CASE 3', 'OR', 'O', ''),
(19, 4, 2, 1, 'f1', 11, 'f1', 'OR', 'MANDATORY', ''),
(20, 4, 3, 1, 'f2', 12, 'f2', 'OR', 'MANDATORY', ''),
(21, 4, 4, 3, 'f3', 13, 'f3', 'OR', 'OPTIONAL', ''),
(22, 4, 5, 3, 'f4', 14, 'f4', 'OR', 'OPTIONAL', ''),
(23, 5, 1, 0, 'CASE 4', 10, 'CASE 4', 'AND', 'MANDATORY', ''),
(24, 5, 2, 1, 'f1', 11, 'f1', 'AND', 'MANDATORY', ''),
(25, 5, 3, 1, 'f2', 12, 'f2', 'AND', 'MANDATORY', ''),
(26, 5, 4, 3, 'f3', 13, 'f3', 'AND', 'OPTIONAL', ''),
(27, 5, 5, 3, 'f4', 14, 'f4', 'AND', 'OPTIONAL', ''),
(28, 5, 6, 1, 'f5', 15, 'f5', 'AND', 'MANDATORY', ''),
(29, 5, 7, 2, 'f6', 16, 'f6', 'AND', 'MANDATORY', '');

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `config`
--
ALTER TABLE `config`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `id` (`id`);

--
-- Indices de la tabla `features`
--
ALTER TABLE `features`
  ADD UNIQUE KEY `id` (`id`);

--
-- Indices de la tabla `featuressupertypes`
--
ALTER TABLE `featuressupertypes`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `products`
--
ALTER TABLE `products`
  ADD UNIQUE KEY `idNode` (`idNode`);

--
-- Indices de la tabla `projects`
--
ALTER TABLE `projects`
  ADD UNIQUE KEY `id_2` (`id`),
  ADD KEY `id` (`id`);

--
-- Indices de la tabla `supertypes`
--
ALTER TABLE `supertypes`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `systems`
--
ALTER TABLE `systems`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `system_tree`
--
ALTER TABLE `system_tree`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `config`
--
ALTER TABLE `config`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;
--
-- AUTO_INCREMENT de la tabla `featuressupertypes`
--
ALTER TABLE `featuressupertypes`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=19;
--
-- AUTO_INCREMENT de la tabla `projects`
--
ALTER TABLE `projects`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;
--
-- AUTO_INCREMENT de la tabla `supertypes`
--
ALTER TABLE `supertypes`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;
--
-- AUTO_INCREMENT de la tabla `systems`
--
ALTER TABLE `systems`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT de la tabla `system_tree`
--
ALTER TABLE `system_tree`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=30;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
