DROP TABLE IF EXISTS employee;

CREATE TABLE employee (
  id INT AUTO_INCREMENT  PRIMARY KEY,
  first_name VARCHAR(250) NOT NULL,
  last_name VARCHAR(250) NOT NULL,
  email VARCHAR(250) DEFAULT NULL
);

INSERT INTO employee (first_name, last_name, email) VALUES
  ('Aliko', 'Dangote', 'alko.dangote@gmail.com'),
  ('Bill', 'Gates', 'bill.gates@hotmail.com'),
  ('Folrunsho', 'Alakija', 'folrunsho.alakija@hotmail.com');

DROP TABLE IF EXISTS account;

CREATE TABLE account (
  id INT AUTO_INCREMENT  PRIMARY KEY,
  balance decimal(19, 2) NOT NULL,
  transaction_type VARCHAR(250) NOT NULL,
  date_time TIMESTAMP DEFAULT NULL
);

INSERT INTO account (balance,transaction_type,date_time) VALUES(1000000, 'CREDIT', '2010-05-16 15:36:38');
INSERT INTO account (balance,transaction_type,date_time) VALUES(2000000, 'CREDIT', '2011-05-16 15:36:38');
INSERT INTO account (balance,transaction_type,date_time) VALUES(3000000, 'CREDIT', '2012-05-16 15:36:38');
INSERT INTO account (balance,transaction_type,date_time) VALUES(4000000, 'CREDIT', '2013-05-16 15:36:38');
INSERT INTO account (balance,transaction_type,date_time) VALUES(5000000, 'CREDIT', '2014-05-16 15:36:38');
INSERT INTO account (balance,transaction_type,date_time) VALUES(6000000, 'CREDIT', '2015-05-16 15:36:38');