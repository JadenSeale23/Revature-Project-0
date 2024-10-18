--This schema will model a Krusty Krab employee management system (sounds good for a P0!)
--This will be a one-to-many relationship which is minimum you'll need for P0.
CREATE TABLE roles ( 
	role_id serial PRIMARY KEY, 
	role_title TEXT UNIQUE, 
	role_salary int check(role_salary > 20000) );

INSERT INTO roles (role_title, role_salary)
VALUES ('Manager', 100000), 
	('Fry Cook', 50000), 
	('Cashier', 40000), 
	('Marketing Director', 100000);

SELECT * FROM roles;

CREATE TABLE employees ( 
	employee_id serial PRIMARY KEY, 
	first_name TEXT NOT NULL, 
	last_name TEXT NOT NULL, 
	role_id_fk int REFERENCES 
	roles (role_id));

INSERT INTO employees (
	first_name, last_name, 
	role_id_fk)
	
VALUES ('Eugene', 'Krabs', 1),    
	('Spongebob', 'Squarepants', 2),    
	('Squidward', 'Tentacles', 3),    
	('Patrick', 'Star', 4);

SELECT * FROM employees;

