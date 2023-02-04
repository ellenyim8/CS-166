DROP TABLE IF EXISTS Professor;
DROP TABLE IF EXISTS Project;
DROP TABLE IF EXISTS Works_In;
DROP TABLE IF EXISTS Dept;
DROP TABLE IF EXISTS work_dept;
DROP TABLE IF EXISTS Graduate;
DROP TABLE IF EXISTS work_proj; 

CREATE TABLE Professor
(
	ssn CHAR(11) NOT NULL,
	name CHAR(11) NOT NULL,
	age INTEGER,
	_rank INTEGER, 
	specialty CHAR(12),
	PRIMARY KEY (ssn)
);

CREATE TABLE Project
(
	pno INTEGER,
	sponsor CHAR(11) NOT NULL, 
	start_date date,
	end_date date, 
	budget INTEGER,
	manager_ssn CHAR(11) NOT NULL,
	PRIMARY KEY(pno),
	FOREIGN KEY(manager_ssn) REFERENCES Professor(ssn)
);

CREATE TABLE Works_In
(
	ssn CHAR(11) NOT NULL,
	pno INTEGER NOT NULL,
	PRIMARY KEY(ssn, pno),
	FOREIGN KEY(ssn) REFERENCES Professor(ssn),
	FOREIGN KEY(pno) REFERENCES Project(pno)
);

CREATE TABLE Dept
(
	dno INTEGER,
	dname CHAR(11) NOT NULL,
	office INTEGER,
	chairman_ssn CHAR(11) NOT NULL,
	PRIMARY KEY(dno),
 	FOREIGN KEY(chairman_ssn) REFERENCES Professor(ssn)
);

CREATE TABLE work_dept
(
	time_pc INTEGER,
	ssn CHAR(11) NOT NULL,
	dno INTEGER NOT NULL,
	FOREIGN KEY(ssn) REFERENCES Professor(ssn),
	FOREIGN KEY(dno) REFERENCES Dept(dno)
);

CREATE TABLE Graduate
(
	grad_ssn CHAR(11), 
	name CHAR(11), 
	age INTEGER,
	deg_pg CHAR(10),
	major_dno INTEGER NOT NULL,
	advise_grad_ssn CHAR(11) NOT NULL,
	PRIMARY KEY(grad_ssn),
	FOREIGN KEY(major_dno) REFERENCES Dept(dno)
);

CREATE TABLE work_proj
(
	since date,
	ssn CHAR(11) NOT NULL,
	pno INTEGER NOT NULL,
	superviser_ssn CHAR(11) NOT NULL,
	FOREIGN KEY(ssn) REFERENCES Graduate(grad_ssn),
	FOREIGN KEY(pno) REFERENCES Project(pno),
	FOREIGN KEY(superviser_ssn) REFERENCES Professor(ssn)
);



DROP TABLE IF EXISTS Musicians;
DROP TABLE IF EXISTS Album;
DROP TABLE IF EXISTS Songs;
DROP TABLE IF EXISTS Perform;
DROP TABLE IF EXISTS Instrument;
DROP TABLE IF EXISTS Plays; 
DROP TABLE IF EXISTS Place; 
DROP TABLE IF EXISTS Telephone;
DROP TABLE IF EXISTS Lives;

CREATE TABLE Musicians
(
	ssn CHAR(11),
	name CHAR(50),
	phone CHAR(10),
	address CHAR(50),
	PRIMARY KEY(ssn)

);

CREATE TABLE Album
(
	albumIdentifier INTEGER,
	copyrightDate date NULL, 
	speed CHAR(50),
	title CHAR(100),
	producer_ssn CHAR(11) NOT NULL,
	PRIMARY KEY (albumIdentifier),
	FOREIGN KEY (producer_ssn) REFERENCES Musicians(ssn)
	ON DELETE CASCADE
);


CREATE TABLE Songs
(
	songID INTEGER,
	title CHAR(100), 
	author CHAR(25),
	appears_albumID INTEGER,
	PRIMARY KEY (songID),
	FOREIGN KEY (appears_albumID) REFERENCES Album(albumIdentifier)
	ON DELETE CASCADE
);

CREATE TABLE Perform
(
	musician_ssn CHAR(11) NOT NULL,
	songs_songid INTEGER NOT NULL,
	PRIMARY KEY(musician_ssn, songs_songid),
	FOREIGN KEY(musician_ssn) REFERENCES Musicians(ssn),
	FOREIGN KEY(songs_songid) REFERENCES Songs(songID)
	ON DELETE CASCADE
);

CREATE TABLE Instrument
(
	instrID INTEGER,
	key CHAR(2),
	dname CHAR(15),
	PRIMARY KEY(instrID)
);

CREATE TABLE Plays
(
	musician_ssn CHAR(11) NOT NULL,
	instr_instrID INTEGER NOT NULL,
	PRIMARY KEY(musician_ssn, instr_instrID),
	FOREIGN KEY(musician_ssn) REFERENCES Musicians(ssn),
	FOREIGN KEY(instr_instrID) REFERENCES Instrument(instrID)
	ON DELETE CASCADE
);

CREATE TABLE Place
(
	address CHAR(50),
	PRIMARY KEY(address)
);

CREATE TABLE Telephone
(
	phone_no CHAR(10),
	place_addr CHAR(50) NOT NULL,
	FOREIGN KEY(place_addr) REFERENCES Place(address)
	ON DELETE CASCADE
);

CREATE TABLE Lives
(
	musician_ssn CHAR(11) NOT NULL,
	musician_home_addr CHAR(50) NOT NULL,
	FOREIGN KEY(musician_ssn) REFERENCES Musicians(ssn),
	FOREIGN KEY(musician_home_addr) REFERENCES Place(address)
	ON DELETE CASCADE
);




