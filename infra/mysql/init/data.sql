-- Use the database
USE
hackathon;

-- Insert initial data
INSERT
IGNORE INTO skill (name) VALUES ('C#');
INSERT
IGNORE INTO skill (name) VALUES ('Java');
INSERT
IGNORE INTO skill (name) VALUES ('Python');
INSERT
IGNORE INTO skill (name) VALUES ('Golang');
INSERT
IGNORE INTO skill (name) VALUES ('MSSQL');
INSERT
IGNORE INTO skill (name) VALUES ('MySQL');

INSERT
IGNORE INTO team (name) VALUES ('RD1');
INSERT
IGNORE INTO team (name) VALUES ('RD2');
INSERT
IGNORE INTO team (name) VALUES ('RD3');
INSERT
IGNORE INTO team (name) VALUES ('RD4');
INSERT
IGNORE INTO team (name) VALUES ('STR');
INSERT
IGNORE INTO team (name) VALUES ('PM');
INSERT
IGNORE INTO team (name) VALUES ('SRE');

INSERT
IGNORE INTO vacancy (title) VALUES ('C# Junior Developer');
INSERT
IGNORE INTO vacancy (title) VALUES ('C# Senior Developer');
INSERT
IGNORE INTO vacancy (title) VALUES ('C# Team leader');
INSERT
IGNORE INTO vacancy (title) VALUES ('Java Junior Developer');
INSERT
IGNORE INTO vacancy (title) VALUES ('Java Senior Developer');
INSERT
IGNORE INTO vacancy (title) VALUES ('Java Team leader');
