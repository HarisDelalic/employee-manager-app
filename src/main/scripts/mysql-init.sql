DROP DATABASE IF EXISTS employeemanagerdb;
DROP USER IF EXISTS `employee_manager_db`@`%`;
CREATE DATABASE IF NOT EXISTS employeemanagerdb CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE USER IF NOT EXISTS `employee_manager_db`@`%` IDENTIFIED WITH mysql_native_password BY 'password';
GRANT SELECT, INSERT, UPDATE, DELETE, CREATE, DROP, REFERENCES, INDEX, ALTER, EXECUTE, CREATE VIEW, SHOW VIEW,
CREATE ROUTINE, ALTER ROUTINE, EVENT, TRIGGER ON `employeemanagerdb`.* TO `employee_manager_db`@`%`;
FLUSH PRIVILEGES;