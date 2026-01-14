# How to Run this Application on Docker
I put the image creation stage inside of `maven install`

* based on project name and version it will create the image, and we don't need docker file this in case

# Getting Started

### Requirements
in order to run the application image we need to pull and run mssql db image and create database.


* to pull and run mssql image: follow this:

  `docker run -e "ACCEPT_EULA=Y" -e "SA_PASSWORD=P@ss123Word" -p 1433:1433 --name sqlserver -d mcr.microsoft.com/mssql/server:2022-latest`
* create DB (in this case i used `Dbeaver` and connect to my mssql database):
    * open a script file and run this query to create database

      `Create DataBase TESTDB;`
* run the external app in docker using:

  `docker run -p 8000:8000 book-management:1.0.0`