-- Initial SQL commands to initialize a local postgres instance with for development.
CREATE DATABASE admin_db;
CREATE ROLE dbuser WITH LOGIN ENCRYPTED PASSWORD 'dbpwd';