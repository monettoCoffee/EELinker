-- Valentina Studio --
-- MySQL dump --
-- ---------------------------------------------------------


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
-- ---------------------------------------------------------


-- CREATE DATABASE "test" ----------------------------------
CREATE DATABASE IF NOT EXISTS `test` CHARACTER SET latin1 COLLATE latin1_swedish_ci;
USE `test`;
-- ---------------------------------------------------------


-- CREATE TABLE "eel_user" -------------------------------------
CREATE TABLE `eel_user` ( 
	`name` VarChar( 255 ) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
	`password` VarChar( 255 ) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL )
CHARACTER SET = latin1
COLLATE = latin1_swedish_ci
ENGINE = InnoDB;
-- -------------------------------------------------------------


-- Dump data of "eel_user" ---------------------------------
INSERT INTO `eel_user`(`name`,`password`) VALUES 
( '1', 'a' ),
( '2', 'b' );
-- ---------------------------------------------------------


/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
-- ---------------------------------------------------------


