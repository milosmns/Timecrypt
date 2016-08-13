-- Old version, MySQL only

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";

DROP TABLE IF EXISTS `message`;
CREATE TABLE IF NOT EXISTS `message` (
  `view_times` int(11) NOT NULL DEFAULT '1' COMMENT 'How many reads are left',
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Message identifier',
  `lifetime` date DEFAULT '2015-01-01' COMMENT 'Date until message is valid',
  `email` varchar(50) DEFAULT NULL COMMENT 'Email for message read notification (encrypted)',
  `text_encrypted` varchar(2500) NOT NULL COMMENT 'Message text (encrypted)',
  `title` varchar(30) DEFAULT NULL COMMENT 'Message title',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COMMENT='Table with all messages' AUTO_INCREMENT=1053 ;