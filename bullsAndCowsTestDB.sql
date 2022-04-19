DROP DATABASE IF EXISTS bullsAndCowsTestDB;
CREATE DATABASE bullsAndCowsTestDB;

USE bullsAndCowsTestDB;

CREATE TABLE Game(
GameId INT PRIMARY KEY AUTO_INCREMENT,
GameStatus VARCHAR(15) NOT NULL,
Answer SMALLINT);

CREATE TABLE Guess(
GuessId INT NOT NULL,
GameId INT NOT NULL,
Attempt CHAR(4) NOT NULL,
Outcome CHAR(7) NOT NULL,
GuessTimeStamp VARCHAR(20) NOT NULL,
PRIMARY KEY(GuessId,GameId),
CONSTRAINT GameId
FOREIGN KEY(GameId)
REFERENCES bullsAndCowsTestDB.Game(GameId));