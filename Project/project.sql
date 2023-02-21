DROP TABLE IF EXISTS Hotel, Rooms, Users, Customer, Manager, MaintCo, Books, Requests, Repairs CASCADE;

CREATE TABLE Users (
        userID INTEGER UNIQUE NOT NULL,
        password CHAR(11) NOT NULL,
        email CHAR(50),
        PRIMARY KEY (userID)
);

CREATE TABLE Customer (
        rewardpts INTEGER NOT NULL,
        address CHAR(100),
        phoneno CHAR(12),
        userID INTEGER,
	PRIMARY KEY(userID),
        FOREIGN KEY(userID) REFERENCES Users(userID)
        ON DELETE CASCADE
);

CREATE TABLE Manager (
        degree CHAR(20),
        salary INTEGER NOT NULL,
	userID INTEGER,
	PRIMARY KEY(userID),
        FOREIGN KEY(userID) REFERENCES Users(userID)
        ON DELETE CASCADE
);

CREATE TABLE MaintCo (
        coID INTEGER UNIQUE NOT NULL,
        name CHAR(20),
        address CHAR(50) NOT NULL,
        PRIMARY KEY (coID)
);

CREATE TABLE Hotel ( 
	hotelID INTEGER NOT NULL, 
	hName CHAR(30) NOT NULL,
	latitude DECIMAL(8,6) NOT NULL,
	longitude DECIMAL(9,6) NOT NULL,
	dateEst date,
	PRIMARY KEY(hotelID),
	FOREIGN KEY (hotelID) REFERENCES Manager(userID)
);

CREATE TABLE Rooms (
	roomNo INTEGER NOT NULL,
	pricePerDay INTEGER NOT NULL,
	imgurl CHAR(100),
	hotelID INTEGER,
	coID INTEGER,
	userID INTEGER,
	PRIMARY KEY(hotelID, coID, userID),
	FOREIGN KEY(hotelID) REFERENCES Hotel(hotelID),
	FOREIGN KEY(coID) REFERENCES MaintCo(coID),
	FOREIGN KEY(userID) REFERENCES Customer(userID)
	ON DELETE CASCADE
);

CREATE TABLE Books(
	dateBooking date,
	stayduration INTEGER,
	bill decimal(3,2),
	userid INTEGER,
	PRIMARY KEY (userid),
	FOREIGN KEY (userid) REFERENCES Customer(userID)
);

CREATE TABLE Repairs (
	coID INTEGER,
	PRIMARY KEY(coID),
	FOREIGN KEY(coID) REFERENCES MaintCo(coID)
);

CREATE TABLE Requests ( 
	userID INTEGER,
	coID INTEGER,
	PRIMARY KEY(userID, coID),
	FOREIGN KEY(userID) REFERENCES Manager(userID),
	FOREIGN KEY(coID) REFERENCES Repairs(coID)
);

