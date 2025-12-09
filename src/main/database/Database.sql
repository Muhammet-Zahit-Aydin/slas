CREATE DATABASE LibraryDB ;

CREATE TABLE LibraryDB.dbo.Books (

    ID IDENTITY(1,1) PRIMARY KEY ,
    Title VARCHAR(100) NOT NULL ,
    Author VARCHAR(100) NOT NULL ,
    Genre VARCHAR(100) NOT NULL ,
    Isbn VARCHAR(13) UNIQUE NOT NULL ,
    Published_Year INT CHECK (Published_Year > 0) ,
    Price DECIMAL(10, 2) CHECK (Price >= 0) ,

) ;

CREATE TABLE LibraryDB.dbo.Users (

    ID IDENTITY(1,1) PRIMARY KEY ,
    First_Name VARCHAR(50) NOT NULL ,
    Last_Name VARCHAR(50) NOT NULL ,
    Email VARCHAR(100) UNIQUE NOT NULL ,
    Member_Type VARCHAR(50) NOT NULL ,
    Password_Hash VARCHAR(256) NOT NULL ,

) ;

CREATE TABLE LibraryDB.dbo.Stock (

    ID IDENTITY(1,1) PRIMARY KEY ,
    Book_ID INT FOREIGN KEY REFERENCES LibraryDB.dbo.Books(ID) ,
    Quantity INT CHECK (Quantity >= 0) ,

) ;

CREATE TABLE LibraryDB.dbo.Lends (

    ID IDENTITY(1,1) PRIMARY KEY ,
    Book_ID INT FOREIGN KEY REFERENCES LibraryDB.dbo.Books(ID) ,
    Member_ID INT FOREIGN KEY REFERENCES LibraryDB.dbo.Members(ID) ,
    Lend_Date DATE NOT NULL ,
    Return_Date DATE ,

) ;

CREATE TABLE LibraryDB.dbo.Penalties (

    ID IDENTITY(1,1) PRIMARY KEY ,
    Member_ID INT FOREIGN KEY REFERENCES LibraryDB.dbo.Members(ID) ,
    Amount DECIMAL(10, 2) CHECK (Amount >= 0) ,
    Reason VARCHAR(255) NOT NULL ,

) ;