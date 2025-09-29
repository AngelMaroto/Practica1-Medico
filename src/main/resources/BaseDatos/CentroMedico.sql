DROP DATABASE IF EXISTS CentroMedico;
CREATE DATABASE CentroMedico;
Use CentroMedico;

CREATE TABLE Paciente(
idP int unsigned auto_increment primary key,
DNI varchar(9),
nombre varchar (20),
pass varchar(64),
direccion varchar(100),
telefono varchar(15)
);

CREATE TABLE Especialidad(
idE int unsigned auto_increment primary key,
tipo enum('Cirugía','Pediatría','Cardiología','Oftalmología','Dermatología')
);

CREATE TABLE Cita(
NCita int unsigned auto_increment primary key,
fecha date,
fk_idE_Especialidad int unsigned,
fk_idP_Paciente int unsigned
);

--Claves foráneas
ALTER TABLE Cita
ADD FOREIGN KEY(fk_idP_Paciente)
REFERENCES Paciente(idP);

ALTER TABLE Cita
ADD FOREIGN KEY(fk_idE_Especialidad)
REFERENCES Especialidad(idE);

--INSERT paciente
INSERT INTO Paciente VALUES
(1,'71188856R','Carlos', SHA2('carlos',256),'c/Cortada n3','623845672'),
(2,'52900473H','María',SHA2('maria' ,256),'Av. Libertad 45','612334567'),
(3,'83529104L','José',SHA2('jose' ,256),'c/Mayor 12','634556789'),
(4,'74219835M','Lucía',SHA2('lucia' ,256),'c/Colón 89','654123987'),
(5,'61983725Z','Antonio',SHA2('antonio' ,256),'Plaza España 3','622789345'),
(6,'98321745B','Laura','laura','c/Sevilla 77','699223344'),
(7,'71593284F','Javier','javier','Av. Andalucía 102','633445566'),
(8,'85219473D','Elena','elena','c/Granada 15','611778899'),
(9,'79138462T','Miguel','miguel','c/Sol 5','624112233'),
(10,'63429175K','Ana','ana','Av. de la Paz 66','688990011');
