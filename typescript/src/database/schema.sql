DROP TABLE IF EXISTS users;

CREATE TABLE users (
    id VARCHAR(100) PRIMARY KEY NOT NULL,
    username VARCHAR(200) NOT NULL,
    password VARCHAR(200) NOT NULL,
    about VARCHAR(200) NULL
);