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

##### Docker

###### Run Docker compose 
docker-compose build && docker-compose up

###### Accessing Container's bash
docker exec -ti reservation-ms /bin/bash

## Using the Application
- Download [Postman](https://www.getpostman.com/);
- Import Postman collections from `~/Documentation/API/RES-DOC-ApiCalls.postman_collection.json`;
- Configure your hostname for where the service is running;
- Use the Valid and Invalid calls to use the system.

## Testing the system
- In the root folder run `mvn test` and it will run all available tests;

### Maven 

#### Running all tests
mvn test

#### Running integration tests
mvn test-compile failsafe:integration-test failsafe:verify 

## Technical Tips/Points
- Remember to add quotes on entity names, as keywords might lead to errors while running automatic queries. Ex.: User entity.
- Using UUID.randomUUID().toString() for the Booking ref, according to what I've found, version 4 of UUID, the risk of collision is 1 in 103 trillion.
- Check the folder Documentation/API to find some sample API Calls.
- The server runs in http://localhost:8484, check application.yml for more details.
- If running using Docker the server will be in http://localhost:8585, check docker-compose.yml for more details.


### Functionalities

- [x] Check availability
- [x] Make a reservation
- [x] Modify a reservation
- [x] Cancel a reservation
- [x] Find all (reference only)
- [x] Integration Test Cases (API) {Still some work and more coverage, but Postman folder contains success and failure calls}
- [x] Fix maven spring boot run
- [x] Integrate Unit Test Cases - Unit Tests for Code and API
- [ ] Increase Unit Test Coverage + More business related tests
- [ ] Prepare H2 in memory for Integration Tests
- [ ] Named Queries migration to DAO and create a Booking with fetch on Guest+Room to avoid N+1
- [x] Docker
- [ ] Deployment