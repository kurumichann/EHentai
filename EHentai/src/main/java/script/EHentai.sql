CREATE TABLE EHentai (
  id int(10) NOT NULL AUTO_INCREMENT,
  url varchar(80) NOT NULL,
  honName varchar(150) NOT NULL,
  category varchar(20) NOT NULL,
  uploader varchar(30) DEFAULT NULL,
  postTime date DEFAULT NULL,
  fileSize decimal(10,0) NOT NULL,
  length smallint(6) NOT NULL,
  favoratedTimes mediumint(9) DEFAULT NULL,
  ratingCount mediumint(9) DEFAULT NULL,
  ratingLabel decimal(4,3) DEFAULT NULL,
  groupCategory text DEFAULT NULL,
  femaleCategory text DEFAULT NULL,
  maleCategory text DEFAULT NULL,
  language varchar(70) DEFAULT NULL,
  characters text DEFAULT NULL,
  artist text DEFAULT NULL,
  parody text DEFAULT NULL,
  misc text DEFAULT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY `honName_unique` (`honName`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

SET FOREIGN_KEY_CHECKS = 1;