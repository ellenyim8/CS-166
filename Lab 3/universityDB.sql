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


