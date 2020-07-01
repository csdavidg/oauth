DROP TABLE IF EXISTS client;

CREATE TABLE client (
  id INT AUTO_INCREMENT  PRIMARY KEY,
  client_id VARCHAR(250) NOT NULL,
  client_secret VARCHAR(250) NOT NULL,
  redirect_uri VARCHAR(250) NOT NULL,
  state VARCHAR(250),
  authorization_code VARCHAR(250)
);

INSERT INTO client (client_id, client_secret, redirect_uri) VALUES
    ('client-1-2020', 'client1', 'http://oauth.demo.com/code/callback'),
    ('client-2-2021', 'client2', 'http://oauth.demo.com/credentials/callback');