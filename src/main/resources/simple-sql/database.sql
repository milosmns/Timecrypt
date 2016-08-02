-- Host: 188.121.44.161
-- Generation Time: Mar 21, 2014 at 02:02 PM
-- Server version: 5.5.33
-- PHP Version: 5.1.6

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";

--
-- Database: `singlenote`
--

-- --------------------------------------------------------

--
-- Table structure for table `note`
--
-- Creation: Jan 28, 2014 at 04:33 PM
--

DROP TABLE IF EXISTS `note`;
CREATE TABLE IF NOT EXISTS `note` (
  `view_times` int(11) NOT NULL DEFAULT '1' COMMENT 'How many times more is it viewable',
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Note identifier',
  `lifetime` date DEFAULT '2015-01-01' COMMENT 'Lifetime of the note, until is the note valid',
  `email` varchar(50) DEFAULT NULL COMMENT 'Email to notify when note is read',
  `text_encrypted` varchar(2500) NOT NULL COMMENT 'Note text',
  `title` varchar(30) DEFAULT NULL COMMENT 'Note title (if provided)',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COMMENT='Table of all notes with data' AUTO_INCREMENT=1053 ;
