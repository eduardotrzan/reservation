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
1 - mvn spring-boot:run (not working yet)
    
2 - run as java application the class: Application.java

##### Docker

###### Run Docker compose 
docker-compose build && docker-compose up

###### Accessing Container's bash
docker exec -ti reservation-ms /bin/bash

## Using the Application


## Technical Tips/Points
- Remember to add quotes on entity names, as keywords might lead to errors while running automatic queries. Ex.: User entity.
- Using UUID.randomUUID().toString() for the Booking ref, according to what I've found, version 4 of UUID, the risk of collision is 1 in 103 trillion.
- Check the folder Documentation/API to find some sample API Calls.
- The server runs in http://localhost:8484, check application.yml for more details.


### Functionalities

- [x] Check availability
- [x] Make a reservation
- [x] Modify a reservation
- [x] Cancel a reservation
- [x] Find all (reference only)
- [x] Integration Test Cases (API) {Still some work and more coverage, but Postman folder contains success and failure calls}
- [ ] Fix maven spring boot run
- [ ] Unit Test Cases (TestNG)
- [ ] Named Queries migration to DAO and create a Booking with fetch on Guest+Room to avoid N+1
- [x] Docker
- [ ] Deployment