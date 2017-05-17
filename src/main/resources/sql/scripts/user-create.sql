create table user(
ID INT(11) NOT NULL AUTO_INCREMENT,
NAME VARCHAR(100),
EMAIL VARCHAR(100) NOT NULL,
PASSWORD VARCHAR(128) NOT NULL,
SALT VARCHAR(30) NOT NULL,
PRIMARY KEY (ID),
UNIQUE KEY email_UNIQUE (EMAIL)
);