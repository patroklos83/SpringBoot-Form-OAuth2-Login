

-- User user/pass
INSERT INTO users (username, password, enabled)
values ('user','$2a$10$H5W/smLkv.tspRGfNuTVvu4zgDPNZBRSsQuXrzAz4ZRRmyfGJMv1i',1);

INSERT INTO authorities (username, authority)
values ('user', 'ROLE_USER');
