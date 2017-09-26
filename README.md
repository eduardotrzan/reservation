# Reservation

This is a Reservation Rest Service.

## Local use

### Creating a database

Pre-requirement: Have Postgres 9.6 installed.

Run in terminal: `createdb -h localhost -p 5432 -U root reservation`

### Running application (one of the following)

#### Pre-requirement: 
- Have Java 9 installed;
- Make sure to add `--add-modules java.xml.bind` as VM Options, otherwise you'll have `JAXBException`.

#### Run Options
1 - mvn spring-boot:run
    
2 - run as java application the class: Application.java

## Using the Application


## Technical Tips
- Remember to add quotes on entity names, as keywords might lead to errors while running automatic queries. Ex.: User entity.
