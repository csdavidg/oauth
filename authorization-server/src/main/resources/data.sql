DROP TABLE IF EXISTS client;

CREATE TABLE client (
  id INT AUTO_INCREMENT  PRIMARY KEY,
  client_id VARCHAR(250) NOT NULL,
  client_secret VARCHAR(250) NOT NULL,
  authorization_code VARCHAR(250)
);

INSERT INTO client (client_id, client_secret) VALUES
    ('client-1-2020', 'client1'),
    ('client-2-2021', 'client2'),
    ('client-3-2022', 'client3');