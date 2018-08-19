DROP TABLE icos IF EXISTS;
DROP TABLE res_form IF EXISTS;
DROP TABLE res_region IF EXISTS;
DROP TABLE res_unit IF EXISTS;
DROP TABLE res_institution IF EXISTS;

CREATE TABLE icos(ico VARCHAR(30) PRIMARY KEY NOT NULL, form INT(11) NOT NULL, lastUpdated DATETIME DEFAULT NULL);
CREATE TABLE res_form(code varchar(30) NOT NULL PRIMARY KEY, text varchar(255) DEFAULT NULL);
CREATE TABLE res_region(code varchar(30) NOT NULL PRIMARY KEY, text varchar(255) DEFAULT NULL);
CREATE TABLE res_unit(code varchar(30) NOT NULL PRIMARY KEY, text varchar(255) DEFAULT NULL);
CREATE TABLE res_institution (
  id int(11) NOT NULL PRIMARY KEY,
  ico varchar(30) DEFAULT NULL,
  name varchar(255) DEFAULT NULL,
  created date DEFAULT NULL,
  ceased date DEFAULT NULL,
  address varchar(255) DEFAULT NULL,
  form_id varchar(30) DEFAULT NULL,
  region_id varchar(30) DEFAULT NULL,
  unit_id varchar(30) DEFAULT NULL,
  CONSTRAINT `fk_institution_unit` FOREIGN KEY (`unit_id`) REFERENCES `res_unit` (`code`),
  CONSTRAINT `fk_institution_form` FOREIGN KEY (`form_id`) REFERENCES `res_form` (`code`),
  CONSTRAINT `fk_institution_region` FOREIGN KEY (`region_id`) REFERENCES `res_region` (`code`)
);

INSERT INTO icos(ico, form, lastUpdated) VALUES('00000175', 331, '2016-07-09 01:02:03.111');
INSERT INTO icos(ico, form, lastUpdated) VALUES('00000078', 301, '2015-01-06 00:01:02.999');
